package ru.gkomega.api.openparser.xml.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ParseException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Role;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.transaction.PlatformTransactionManager;
import ru.gkomega.api.openparser.batch.handler.CustomJobListener;
import ru.gkomega.api.openparser.batch.handler.MultiResourceReader;
import ru.gkomega.api.openparser.batch.property.BatchProperty;
import ru.gkomega.api.openparser.xml.handler.*;
import ru.gkomega.api.openparser.xml.model.dto.CatalogItemDto;
import ru.gkomega.api.openparser.xml.model.entity.CatalogItemEntity;
import ru.gkomega.api.openparser.xml.property.XmlResourceProperty;

import java.nio.charset.StandardCharsets;

import static org.springframework.batch.core.ExitStatus.COMPLETED;
import static org.springframework.batch.core.ExitStatus.FAILED;

@Slf4j
@Configuration
@EnableConfigurationProperties(XmlResourceProperty.class)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Description("OpenParser Xml Batch configuration")
public class XmlBatchConfiguration {
    /**
     * Default bean naming conventions
     */
    public static final String DATA_XML_UNMARSHALLER_BEAN_NAME = "xmlDataUnmarshaller";
    //public static final String DATA_CSV_READER_BEAN_NAME = "csvFileDataReader";
    public static final String DATA_XML_READER_BEAN_NAME = "xmlFileDataReader";

    public static final String JOB_CATALOG_DATA_LOADER_BEAN_NAME = "catalogDataLoaderJob";

    public static final String STEP_LOAD_DATA_BEAN_NAME = "LoadCatalogItemsFromFile";
    public static final String STEP_CALCULATE_STATISTICS_BEAN_NAME = "CalculateCatalogItemsStatistics";
    public static final String STEP_NOTIFIER_BEAN_NAME = "NotifyInvalidCatalogItems";

//    @Bean(CSV_DATA_READER_BEAN_NAME)
//    @StepScope
//    public FlatFileItemReader<CatalogItemDto> csvCatalogDataReader() {
//        return new FlatFileItemReaderBuilder<CatalogItemDto>()
//                .name("catalogItemReader")
//                .resource(new ClassPathResource("data/template.csv"))
//                .delimited()
//                .names("identification", "currency", "ammount", "accountType", "accountNumber", "description", "firstLastName")
//                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
//                    this.setTargetType(CatalogItemDto.class);
//                }})
//                .build();
//    }

    @Bean(DATA_XML_UNMARSHALLER_BEAN_NAME)
    @Description("Catalog item xml unmarshaller bean")
    public Unmarshaller xmlCatalogDataUnmarshaller() {
        final XStreamMarshaller unmarshaller = new XStreamMarshaller();
        //unmarshaller.getXStream().registerLocalConverter(CatalogItemDto.class, "refKey", new UuidConverter());
//        unmarshaller.setConverters(new UuidConverter());
        unmarshaller.setAnnotatedClasses(CatalogItemDto.class);
//        unmarshaller.getXStream().processAnnotations(CatalogItemDto.class);
        unmarshaller.setEncoding(StandardCharsets.UTF_8.name());
//        unmarshaller.setProcessExternalEntities(false);
        unmarshaller.setAutodetectAnnotations(true);
//        unmarshaller.setSupportDtd(true);
        unmarshaller.afterPropertiesSet();
        return unmarshaller;
    }

    @Bean(DATA_XML_READER_BEAN_NAME)
    @StepScope
    @Description("Catalog item xml reader bean")
    public ItemReader<CatalogItemDto> xmlCatalogDataReader(final XmlResourceProperty xmlResourceProperty,
                                                           @Qualifier(DATA_XML_UNMARSHALLER_BEAN_NAME) final Unmarshaller unmarshaller) {
        return new MultiResourceReader<>(xmlResourceProperty, new XmlFileCatalogItemReader(xmlResourceProperty, unmarshaller));
    }

//    @Bean(DATA_CSV_READER_BEAN_NAME)
//    @StepScope
//    public ItemReader<CatalogItemDto> csvCatalogDataReader(final XmlResourceProperty xmlResourceProperty) {
//        return new MultiResourceReader<>(xmlResourceProperty, new CsvFileCatalogDataReader(xmlResourceProperty));
//    }

//    @Bean(DATA_BATCH_WRITER_BEAN_NAME)
//    public JdbcBatchItemWriter<CatalogItemEntity> databaseCatalogDataBatchWriter(@Qualifier("dataSource") final DataSource dataSource) {
//        final JdbcBatchItemWriterBuilder<CatalogItemEntity> jdbcBatchItemWriter = new JdbcBatchItemWriterBuilder<>();
//        jdbcBatchItemWriter.assertUpdates(true);
//        jdbcBatchItemWriter.beanMapped();
//        jdbcBatchItemWriter.dataSource(dataSource);
//        jdbcBatchItemWriter.sql(
//                " INSERT INTO contact_info( ref_key, line_number, contact_type, view_key, phone, country, region, city, address, domain_name, view_list_key, active_at )" +
//                        " VALUES ( :refKey , :lineNumber, :contactType, :viewKey, :phone, :country, :region, :city, :address, :domainName, :viewListKey, :activeAt ) ");
//        return jdbcBatchItemWriter.build();
//    }

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
                             @Qualifier(DATA_XML_READER_BEAN_NAME) final ItemReader<CatalogItemDto> itemReader,
                             final CatalogItemProcessor itemProcessor,
                             final CatalogItemWriter itemWriter) {
        return stepBuilderFactory.get(STEP_LOAD_DATA_BEAN_NAME)
            .<CatalogItemDto, CatalogItemEntity>chunk(batchProperty.getChunkSize())
            .reader(itemReader)
            .processor(itemProcessor)
            .writer(itemWriter)
            .faultTolerant()
            .skip(ParseException.class)
            .transactionManager(platformTransactionManager)
            .build();
    }

//    @Bean(STEP_VALIDATE_DATA_BEAN_NAME)
//    public Step validateDataStep(final BatchProperty batchProperty,
//                                 final StepBuilderFactory stepBuilderFactory,
//                                 final PlatformTransactionManager platformTransactionManager,
//                                 @Qualifier(DATA_VALIDATOR_BEAN_NAME) final CatalogItemValidator itemValidator) {
//        return stepBuilderFactory.get(STEP_VALIDATE_DATA_BEAN_NAME).<CatalogItemEntity, CatalogItemEntity>chunk(batchProperty.getChunkSize())
//                .processor(itemValidator)
//                .transactionManager(platformTransactionManager)
//                .build();
//    }

//    @Bean(STEP_WRITE_DATA_BEAN_NAME)
//    public Step writeDataStep(final BatchProperty batchProperty,
//                              final StepBuilderFactory stepBuilderFactory,
//                              final PlatformTransactionManager platformTransactionManager,
//                              @Qualifier(DATA_VALIDATOR_BEAN_NAME) final CatalogItemValidator itemValidator,
//                              @Qualifier(DATA_WRITER_BEAN_NAME) final ItemWriter<CatalogItemEntity> itemWriter) {
//        return stepBuilderFactory.get(STEP_WRITE_DATA_BEAN_NAME).<CatalogItemEntity, CatalogItemEntity>chunk(batchProperty.getChunkSize())
//                .processor(itemValidator)
//                .writer(itemWriter)
//                .transactionManager(platformTransactionManager)
//                .build();
//    }

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
