package ru.gkomega.api.openparser.file.configuration;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.database.HibernateItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder;
import org.springframework.batch.item.file.builder.MultiResourceItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Role;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.transaction.PlatformTransactionManager;
import ru.gkomega.api.openparser.batch.handler.CustomJobListener;
import ru.gkomega.api.openparser.batch.property.BatchProperty;
import ru.gkomega.api.openparser.file.handler.CalculateStatisticsTasklet;
import ru.gkomega.api.openparser.file.handler.FlatFileCatalogItemReader;
import ru.gkomega.api.openparser.file.handler.FlatFileCatalogItemWriter;
import ru.gkomega.api.openparser.file.handler.LoggingTasklet;
import ru.gkomega.api.openparser.file.model.CustomerCreditDto;
import ru.gkomega.api.openparser.file.property.FileResourceProperty;

import javax.sql.DataSource;
import java.io.IOException;

import static org.springframework.batch.core.ExitStatus.COMPLETED;
import static org.springframework.batch.core.ExitStatus.FAILED;

@Slf4j
@Configuration
@EnableConfigurationProperties(FileResourceProperty.class)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Description("OpenParser File Batch configuration")
public class FileBatchConfiguration {
    /**
     * Default bean naming conventions
     */
    public static final String DATA_FILE_READER_BEAN_NAME = "fileDataReader";
    public static final String DATA_FILE_WRITER_BEAN_NAME = "fileDataWriter";
    public static final String DATA_DB_WRITER_BEAN_NAME = "databaseDataWriter";
    public static final String DATA_FORMATTED_FILE_WRITER_BEAN_NAME = "formattedFileDataWriter";
    public static final String DATA_FILE_MULTI_READER_BEAN_NAME = "fileMultiDataReader";
    public static final String DATA_FILE_MULTI_WRITER_BEAN_NAME = "fileMultiDataWriter";
    public static final String DATA_DB_BATCH_WRITER_BEAN_NAME = "databaseBatchDataWriter";

    public static final String JOB_CATALOG_DATA_LOADER_BEAN_NAME = "dataLoaderJob";

    public static final String STEP_LOAD_DATA_BEAN_NAME = "LoadCatalogItemsFromFile";
    public static final String STEP_CALCULATE_STATISTICS_BEAN_NAME = "CalculateCatalogItemsStatistics";
    public static final String STEP_NOTIFIER_BEAN_NAME = "NotifyInvalidCatalogItems";

    @Bean(DATA_FILE_READER_BEAN_NAME)
    @StepScope
    public FlatFileItemReader<CustomerCreditDto> itemReader() {
        return new FlatFileItemReaderBuilder<CustomerCreditDto>()
            .name("customerCreditReader")
            .resource(new ClassPathResource("data/template.txt"))
            .delimited()
            .delimiter("|")
            .names(new String[]{"name", "credit"})
            .build();
    }

    @Bean(DATA_FILE_WRITER_BEAN_NAME)
    @StepScope
    public FlatFileItemWriter<CustomerCreditDto> itemWriter() {
        return new FlatFileItemWriterBuilder<CustomerCreditDto>()
            .name("customerCreditWriter")
            .resource(new ClassPathResource("data/output.txt"))
            .delimited()
            .delimiter("|")
            .names(new String[]{"name", "credit"})
            .build();
    }

    @Bean(DATA_FILE_MULTI_READER_BEAN_NAME)
    @StepScope
    public ItemReader<CustomerCreditDto> fileCatalogDataReader(final FileResourceProperty fileResourceProperty) throws IOException {
        final ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        final Resource[] resources = patternResolver.getResources(fileResourceProperty.getPathPattern());

        return new MultiResourceItemReaderBuilder<CustomerCreditDto>()
            .name("fileCatalogDataReader")
            .saveState(false)
            .setStrict(true)
            .resources(resources)
            .delegate(new FlatFileCatalogItemReader(fileResourceProperty))
            .build();
    }

    @Bean(DATA_FILE_MULTI_WRITER_BEAN_NAME)
    @StepScope
    public ItemWriter<CustomerCreditDto> fileCatalogDataWriter(final FileResourceProperty fileResourceProperty) throws IOException {
        final ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        final Resource resource = patternResolver.getResource(fileResourceProperty.getPathPattern());

        return new MultiResourceItemWriterBuilder<CustomerCreditDto>()
            .name("fileCatalogDataWriter")
            .saveState(false)
            .resource(resource)
            .delegate(new FlatFileCatalogItemWriter(fileResourceProperty))
            .build();
    }

    @Bean(DATA_FORMATTED_FILE_WRITER_BEAN_NAME)
    public FlatFileItemWriter<CustomerCreditDto> formattedItemWriter() {
        return new FlatFileItemWriterBuilder<CustomerCreditDto>()
            .name("customerCreditFormattedWriter")
            .resource(new ClassPathResource("data/output.txt"))
            .formatted()
            .format("%-9s%-2.0f")
            .names(new String[]{"name", "credit"})
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

    @Bean(DATA_DB_WRITER_BEAN_NAME)
    @StepScope
    public ItemWriter<CustomerCreditDto> itemWriter(final SessionFactory sessionFactory) {
        final HibernateItemWriter<CustomerCreditDto> itemWriter = new HibernateItemWriter<>();
        itemWriter.setSessionFactory(sessionFactory);
        return itemWriter;
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
                             @Qualifier(DATA_FILE_READER_BEAN_NAME) final ItemReader<CustomerCreditDto> itemReader,
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
}
