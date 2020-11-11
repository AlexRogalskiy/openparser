package ru.gkomega.api.openparser.csv.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
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
import ru.gkomega.api.openparser.batch.handler.MultiResourceReader;
import ru.gkomega.api.openparser.batch.property.BatchProperty;
import ru.gkomega.api.openparser.csv.handler.CalculateStatisticsTasklet;
import ru.gkomega.api.openparser.csv.handler.CsvFileCatalogItemReader;
import ru.gkomega.api.openparser.csv.handler.LoggingTasklet;
import ru.gkomega.api.openparser.csv.model.CatalogItemDto;
import ru.gkomega.api.openparser.csv.property.CsvResourceProperty;

import javax.sql.DataSource;
import java.io.IOException;

import static org.springframework.batch.core.ExitStatus.COMPLETED;
import static org.springframework.batch.core.ExitStatus.FAILED;

@Slf4j
@Configuration
@EnableConfigurationProperties(CsvResourceProperty.class)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Description("OpenParser Csv Batch configuration")
public class CsvBatchConfiguration {
    /**
     * Default bean naming conventions
     */
    public static final String DATA_CSV_READER_BEAN_NAME = "csvFileDataReader";
    public static final String DATA_CSV_MULTI_READER_BEAN_NAME = "csvFileMultiDataReader";
    public static final String DATA_DB_BATCH_WRITER_BEAN_NAME = "databaseBatchDataWriter";

    public static final String JOB_CATALOG_DATA_LOADER_BEAN_NAME = "dataLoaderJob";

    public static final String STEP_LOAD_DATA_BEAN_NAME = "LoadCatalogItemsFromFile";
    public static final String STEP_CALCULATE_STATISTICS_BEAN_NAME = "CalculateCatalogItemsStatistics";
    public static final String STEP_NOTIFIER_BEAN_NAME = "NotifyInvalidCatalogItems";

    @Bean(DATA_CSV_READER_BEAN_NAME)
    @StepScope
    public FlatFileItemReader<CatalogItemDto> csvCatalogDataReader() {
        return new FlatFileItemReaderBuilder<CatalogItemDto>()
            .name("catalogItemReader")
            .resource(new ClassPathResource("data/template.csv"))
            .delimited()
            .names("identification", "currency", "ammount", "accountType", "accountNumber", "description", "firstLastName")
            .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                this.setTargetType(CatalogItemDto.class);
            }})
            .build();
    }

    @Bean(DATA_CSV_MULTI_READER_BEAN_NAME)
    @StepScope
    public ItemReader<CatalogItemDto> csvCatalogDataReader(final CsvResourceProperty csvResourceProperty) {
        return new MultiResourceReader<>(csvResourceProperty, new CsvFileCatalogItemReader(csvResourceProperty));
    }

    @Bean
    public JsonItemReader<CatalogItemDto> jsonItemReader() {
        return new JsonItemReaderBuilder<CatalogItemDto>()
            .jsonObjectReader(new JacksonJsonObjectReader<>(CatalogItemDto.class))
            .resource(new ClassPathResource("trades.json"))
            .name("tradeJsonItemReader")
            .build();
    }

    @Bean
    public JsonFileItemWriter<CatalogItemDto> jsonFileItemWriter() {
        return new JsonFileItemWriterBuilder<CatalogItemDto>()
            .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>())
            .resource(new ClassPathResource("trades.json"))
            .name("tradeJsonFileItemWriter")
            .build();
    }

    @Bean
    public MultiResourceItemReader<CatalogItemDto> multiResourceReader(final CsvResourceProperty csvResourceProperty,
                                                                       final JsonItemReader<CatalogItemDto> jsonItemReader) throws IOException {
        final ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        final Resource[] resources = patternResolver.getResources(csvResourceProperty.getPathPattern());
        return new MultiResourceItemReaderBuilder<CatalogItemDto>()
            .delegate(jsonItemReader)
            .resources(resources)
            .saveState(false)
            .build();
    }

    @Bean(DATA_DB_BATCH_WRITER_BEAN_NAME)
    public JdbcBatchItemWriter<CatalogItemDto> databaseCatalogDataBatchWriter(@Qualifier("dataSource") final DataSource dataSource) {
        final JdbcBatchItemWriterBuilder<CatalogItemDto> jdbcBatchItemWriter = new JdbcBatchItemWriterBuilder<>();
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
                             @Qualifier(DATA_CSV_READER_BEAN_NAME) final ItemReader<CatalogItemDto> itemReader,
                             @Qualifier(DATA_DB_BATCH_WRITER_BEAN_NAME) final JdbcBatchItemWriter<CatalogItemDto> itemWriter) {
        return stepBuilderFactory.get(STEP_LOAD_DATA_BEAN_NAME)
            .<CatalogItemDto, CatalogItemDto>chunk(batchProperty.getChunkSize())
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
