package ru.gkomega.api.openparser.db.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.database.*;
import org.springframework.batch.item.database.builder.*;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Role;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.PlatformTransactionManager;
import ru.gkomega.api.openparser.batch.handler.CustomJobListener;
import ru.gkomega.api.openparser.batch.property.BatchProperty;
import ru.gkomega.api.openparser.db.handler.CalculateStatisticsTasklet;
import ru.gkomega.api.openparser.db.handler.LoggingTasklet;
import ru.gkomega.api.openparser.db.model.CustomerCreditDto;
import ru.gkomega.api.openparser.db.model.CustomerCreditDtoRowMapper;
import ru.gkomega.api.openparser.db.property.DbResourceProperty;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.batch.core.ExitStatus.COMPLETED;
import static org.springframework.batch.core.ExitStatus.FAILED;

@Slf4j
@Configuration
@EnableConfigurationProperties(DbResourceProperty.class)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Description("OpenParser Database Batch configuration")
public class DbBatchConfiguration {
    /**
     * Default bean naming conventions
     */
    public static final String DATA_DB_READER_BEAN_NAME = "databaseFileDataReader";
    public static final String DATA_DB_BATCH_WRITER_BEAN_NAME = "databaseBatchDataWriter";

    public static final String JOB_CATALOG_DATA_LOADER_BEAN_NAME = "dataLoaderJob";

    public static final String STEP_LOAD_DATA_BEAN_NAME = "LoadCatalogItemsFromDatabase";
    public static final String STEP_CALCULATE_STATISTICS_BEAN_NAME = "CalculateCatalogItemsStatistics";
    public static final String STEP_NOTIFIER_BEAN_NAME = "NotifyInvalidCatalogItems";

    @Bean(DATA_DB_READER_BEAN_NAME)
    public JdbcCursorItemReader<CustomerCreditDto> itemReader(@Qualifier("dataSource") final DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<CustomerCreditDto>()
            .name("creditReader")
            .dataSource(dataSource)
            .saveState(false)
            .sql("select ID, NAME, CREDIT from CUSTOMER")
            .rowMapper(new CustomerCreditDtoRowMapper())
            .build();
    }

    @Bean
    public HibernateCursorItemReader<CustomerCreditDto> itemReader(final SessionFactory sessionFactory) {
        return new HibernateCursorItemReaderBuilder<CustomerCreditDto>()
            .name("creditReader")
            .saveState(false)
            .sessionFactory(sessionFactory)
            .queryString("from CustomerCredit")
            .build();
    }

    @Bean
    public StoredProcedureItemReader<CustomerCreditDto> reader(@Qualifier("dataSource") final DataSource dataSource) {
        final StoredProcedureItemReader<CustomerCreditDto> reader = new StoredProcedureItemReader<>();
        reader.setDataSource(dataSource);
        reader.setProcedureName("sp_customer_credit");
        reader.setRowMapper(new CustomerCreditDtoRowMapper());
        return reader;
    }

    @Bean
    public JdbcPagingItemReader<CustomerCreditDto> itemReader(@Qualifier("dataSource") final DataSource dataSource,
                                                              final PagingQueryProvider queryProvider) {
        final Map<String, Object> parameterValues = new HashMap<>();
        parameterValues.put("status", "NEW");

        return new JdbcPagingItemReaderBuilder<CustomerCreditDto>()
            .name("creditReader")
            .dataSource(dataSource)
            .queryProvider(queryProvider)
            .parameterValues(parameterValues)
            .rowMapper(new CustomerCreditDtoRowMapper())
            .pageSize(1000)
            .build();
    }

    @Bean
    public JpaPagingItemReader<CustomerCreditDto> itemReader(final EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<CustomerCreditDto>()
            .name("creditReader")
            .entityManagerFactory(entityManagerFactory)
            .queryString("select c from CustomerCredit c")
            .pageSize(1000)
            .build();
    }

    @Bean
    public SqlPagingQueryProviderFactoryBean queryProvider() {
        final SqlPagingQueryProviderFactoryBean provider = new SqlPagingQueryProviderFactoryBean();
        provider.setSelectClause("select id, name, credit");
        provider.setFromClause("from customer");
        provider.setWhereClause("where status=:status");
        provider.setSortKey("id");
        return provider;
    }

    @Bean
    public JsonFileItemWriter<CustomerCreditDto> jsonFileItemWriter() {
        return new JsonFileItemWriterBuilder<CustomerCreditDto>()
            .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>())
            .resource(new ClassPathResource("trades.json"))
            .name("tradeJsonFileItemWriter")
            .build();
    }

    @Bean(DATA_DB_BATCH_WRITER_BEAN_NAME)
    public JdbcBatchItemWriter<CustomerCreditDto> databaseCatalogDataBatchWriter(@Qualifier("dataSource") final DataSource dataSource) {
        final JdbcBatchItemWriterBuilder<CustomerCreditDto> jdbcBatchItemWriter = new JdbcBatchItemWriterBuilder<>();
        jdbcBatchItemWriter.assertUpdates(true);
        jdbcBatchItemWriter.beanMapped();
        jdbcBatchItemWriter.dataSource(dataSource);
        jdbcBatchItemWriter.sql(
            " INSERT INTO contact_info( ref_key, line_number, contact_type, view_key, phone, country, region, city, address, domain_name, view_list_key, active_at )" +
                " VALUES ( :refKey , :lineNumber, :contactType, :viewKey, :phone, :country, :region, :city, :address, :domainName, :viewListKey, :activeAt ) ");
        return jdbcBatchItemWriter.build();
    }

    @Bean(JOB_CATALOG_DATA_LOADER_BEAN_NAME)
    @Description("Catalog data processor job bean")
    public Job catalogDataLoaderJob(final JobBuilderFactory jobBuilders,
                                    final CustomJobListener customJobListener,
                                    @Qualifier(STEP_LOAD_DATA_BEAN_NAME) final Step loadDataStep,
                                    @Qualifier(STEP_CALCULATE_STATISTICS_BEAN_NAME) final Step calculateStep,
                                    @Qualifier(STEP_NOTIFIER_BEAN_NAME) final Step notifierDataStep) {
        return jobBuilders.get(JOB_CATALOG_DATA_LOADER_BEAN_NAME)
            .incrementer(new RunIdIncrementer())
            .listener(customJobListener)
            .start(loadDataStep)
            .on(COMPLETED.getExitCode())
            .to(calculateStep)
            .from(loadDataStep)
            .on(FAILED.getExitCode())
            .to(notifierDataStep)
            .from(loadDataStep)
            .on("*")
            .stop()
            .end()
            .build();
    }

    @Bean(STEP_LOAD_DATA_BEAN_NAME)
    @Description("Catalog data processor step bean")
    public Step loadDataStep(final BatchProperty batchProperty,
                             final StepBuilderFactory stepBuilderFactory,
                             final PlatformTransactionManager platformTransactionManager,
                             @Qualifier(DATA_DB_READER_BEAN_NAME) final ItemReader<CustomerCreditDto> itemReader,
                             @Qualifier(DATA_DB_BATCH_WRITER_BEAN_NAME) final JdbcBatchItemWriter<CustomerCreditDto> itemWriter) {
        return stepBuilderFactory.get(STEP_LOAD_DATA_BEAN_NAME)
            .<CustomerCreditDto, CustomerCreditDto>chunk(batchProperty.getChunkSize())
            .reader(itemReader)
            .writer(itemWriter)
            .faultTolerant()
            .skip(ParseException.class)
            .transactionManager(platformTransactionManager)
            .build();
    }

    @Bean(STEP_CALCULATE_STATISTICS_BEAN_NAME)
    @Description("Catalog data statistics aggregator step bean")
    public Step calculateStatisticsDataStep(final StepBuilderFactory stepBuilderFactory,
                                            final PlatformTransactionManager platformTransactionManager,
                                            final CalculateStatisticsTasklet tasklet) {
        return stepBuilderFactory.get(STEP_CALCULATE_STATISTICS_BEAN_NAME)
            .tasklet(tasklet)
            .transactionManager(platformTransactionManager)
            .build();
    }

    @Bean(STEP_NOTIFIER_BEAN_NAME)
    @Description("Catalog data notifier step bean")
    public Step notifierDataStep(final StepBuilderFactory stepBuilderFactory,
                                 final PlatformTransactionManager platformTransactionManager,
                                 final LoggingTasklet tasklet) {
        return stepBuilderFactory.get(STEP_NOTIFIER_BEAN_NAME)
            .tasklet(tasklet)
            .transactionManager(platformTransactionManager)
            .build();
    }

    @Bean
    public ResourceDatabasePopulator resourceDatabasePopulator(final DbResourceProperty resourceProperty) {
        final ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.setScripts(resourceProperty.getScripts());
        return databasePopulator;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(@Qualifier("dataSource") final DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Configuration
    @RequiredArgsConstructor
    @ConditionalOnProperty(prefix = DbResourceProperty.PROPERTY_PREFIX, value = "scripts")
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Description("OpenParser Database Population configuration")
    public static class DbPopulationConfiguration {

        private final ResourceDatabasePopulator resourceDatabasePopulator;
        private final DataSource dataSource;

        @PostConstruct
        private void init() {
            DatabasePopulatorUtils.execute(this.resourceDatabasePopulator, this.dataSource);
        }
    }
}
