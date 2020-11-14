package ru.gkomega.api.openparser.xml.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Role;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.transaction.PlatformTransactionManager;
import ru.gkomega.api.openparser.batch.handler.CustomJobListener;
import ru.gkomega.api.openparser.batch.property.BatchProperty;
import ru.gkomega.api.openparser.xml.handler.CalculateStatisticsTasklet;
import ru.gkomega.api.openparser.xml.handler.CatalogItemProcessor;
import ru.gkomega.api.openparser.xml.handler.CatalogItemWriter;
import ru.gkomega.api.openparser.xml.handler.LoggingTasklet;
import ru.gkomega.api.openparser.xml.model.dto.CatalogItemDto;
import ru.gkomega.api.openparser.xml.model.entity.CatalogItemEntity;
import ru.gkomega.api.openparser.xml.property.XmlResourceProperty;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.springframework.batch.core.ExitStatus.COMPLETED;
import static org.springframework.batch.core.ExitStatus.FAILED;
import static ru.gkomega.api.openparser.executor.configuration.TaskExecutorConfiguration.AsyncTaskExecutorConfiguration.ASYNC_TASK_EXECUTOR_BEAN_NAME;

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
    public static final String DATA_XML_READER_BEAN_NAME = "xmlFileDataReader";
    public static final String DATA_XML_MULTI_READER_BEAN_NAME = "xmlFileMultiDataReader";

    public static final String JOB_CATALOG_DATA_LOADER_BEAN_NAME = "dataLoaderJob";

    public static final String STEP_LOAD_DATA_BEAN_NAME = "LoadCatalogItemsFromFile";
    public static final String STEP_CALCULATE_STATISTICS_BEAN_NAME = "CalculateCatalogItemsStatistics";
    public static final String STEP_NOTIFIER_BEAN_NAME = "NotifyInvalidCatalogItems";

    @Bean(DATA_XML_UNMARSHALLER_BEAN_NAME)
    @Description("Catalog item xml unmarshaller bean")
    public Unmarshaller unmarshaller() {
        final XStreamMarshaller unmarshaller = new XStreamMarshaller();
        unmarshaller.setAnnotatedClasses(CatalogItemDto.class);
        unmarshaller.setEncoding(StandardCharsets.UTF_8.name());
        unmarshaller.setProcessExternalEntities(false);
        unmarshaller.setAutodetectAnnotations(true);
        unmarshaller.setSupportDtd(true);
        unmarshaller.afterPropertiesSet();
        return unmarshaller;
    }

    @Bean(DATA_XML_READER_BEAN_NAME)
    @StepScope
    @Description("Catalog item synchronized xml reader bean")
    public SynchronizedItemStreamReader<CatalogItemDto> xmlCatalogDataReader(@Qualifier(DATA_XML_MULTI_READER_BEAN_NAME) final MultiResourceItemReader<CatalogItemDto> itemReader) {
        final SynchronizedItemStreamReader<CatalogItemDto> synchronizedItemStreamReader = new SynchronizedItemStreamReader<>();
        synchronizedItemStreamReader.setDelegate(itemReader);
        return synchronizedItemStreamReader;
    }

    @Bean(DATA_XML_MULTI_READER_BEAN_NAME)
    @Description("Catalog item multi resource xml reader bean")
    public MultiResourceItemReader<CatalogItemDto> xmlMultiResourceCatalogDataReader(final XmlResourceProperty xmlResourceProperty,
                                                                                     @Qualifier(DATA_XML_UNMARSHALLER_BEAN_NAME) final Unmarshaller unmarshaller) throws IOException {
        final StaxEventItemReader<CatalogItemDto> xmlFileReader = new StaxEventItemReader<>();
        xmlFileReader.setFragmentRootElementNames(xmlResourceProperty.getRootElements());
        xmlFileReader.setUnmarshaller(unmarshaller);
        xmlFileReader.setSaveState(false);

        final ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        final Resource[] resources = patternResolver.getResources(xmlResourceProperty.getPathPattern());

        return new MultiResourceItemReaderBuilder<CatalogItemDto>()
            .delegate(xmlFileReader)
            .resources(resources)
            .saveState(false)
            .build();
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
                             @Qualifier(ASYNC_TASK_EXECUTOR_BEAN_NAME) final AsyncTaskExecutor taskExecutor,
                             final PlatformTransactionManager platformTransactionManager,
                             @Qualifier(DATA_XML_READER_BEAN_NAME) final ItemStreamReader<CatalogItemDto> itemReader,
                             final CatalogItemProcessor itemProcessor,
                             final CatalogItemWriter itemWriter) {
        return stepBuilderFactory.get(STEP_LOAD_DATA_BEAN_NAME)
            .<CatalogItemDto, CatalogItemEntity>chunk(batchProperty.getChunkSize())
            .reader(itemReader)
            .processor(itemProcessor)
            .writer(itemWriter)
            .faultTolerant()
            .retryLimit(3)
            .retry(DeadlockLoserDataAccessException.class)
            .noRollback(ValidationException.class)
            .readerIsTransactionalQueue()
            .taskExecutor(taskExecutor)
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
