package ru.gkomega.api.openparser.batch.configuration;

import org.springframework.batch.core.jsr.JsrJobParametersConverter;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.ExecutionContextSerializer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import ru.gkomega.api.openparser.batch.handler.CustomExecutionSerializer;
import ru.gkomega.api.openparser.batch.handler.CustomJobListener;
import ru.gkomega.api.openparser.batch.logging.LoggingListener;
import ru.gkomega.api.openparser.batch.property.BatchProperty;

import javax.sql.DataSource;

@Configuration
@Import({LoggingListener.class, CustomJobListener.class})
@ConditionalOnProperty(prefix = BatchProperty.PROPERTY_PREFIX, value = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(BatchProperty.class)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Description("OpenParser Batch configuration")
public abstract class BatchConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(CustomExecutionSerializer.class)
    @Description("Custom batch serializer bean")
    public ExecutionContextSerializer serializer() {
        return new CustomExecutionSerializer();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(DataSource.class)
    @Description("Job parameters converter bean")
    public JsrJobParametersConverter jsrJobParametersConverter(final DataSource dataSource) throws Exception {
        final JsrJobParametersConverter jsrJobParametersConverter = new JsrJobParametersConverter(dataSource);
        jsrJobParametersConverter.afterPropertiesSet();
        return jsrJobParametersConverter;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean({
        DataSource.class,
        PlatformTransactionManager.class
    })
    @Description("Job repository bean")
    public JobRepository jobRepository(final DataSource dataSource,
                                       final PlatformTransactionManager transactionManager) throws Exception {
        final JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(dataSource);
        factory.setTransactionManager(transactionManager);
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Bean
    @ConditionalOnMissingBean
    @Description("Platform transaction manager bean")
    public PlatformTransactionManager transactionManager() {
        return new ResourcelessTransactionManager();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(JobRepository.class)
    @Description("Job launcher bean")
    public JobLauncher jobLauncher(final JobRepository jobRepository,
                                   final TaskExecutor taskExecutor) throws Exception {
        final SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(taskExecutor);
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }
}
