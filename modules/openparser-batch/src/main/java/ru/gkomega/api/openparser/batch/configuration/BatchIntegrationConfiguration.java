package ru.gkomega.api.openparser.batch.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.integration.launch.JobLaunchingGateway;
import org.springframework.batch.integration.launch.JobLaunchingMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import ru.gkomega.api.openparser.batch.property.BatchProperty;

import java.io.File;

@Configuration
@IntegrationComponentScan
@Import(BatchFlowConfiguration.class)
@ConditionalOnProperty(prefix = BatchProperty.PROPERTY_PREFIX, value = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(BatchProperty.class)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Description("OpenParser Batch Integration configuration")
public abstract class BatchIntegrationConfiguration {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job sampleJob;

    @Bean
    public DirectChannel inputChannel() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow sampleFlow(final MessageSource<File> fileReadingMessageSource,
                                      final DirectChannel inputChannel,
                                      final FileMessageToJobRequest fileMessageToJobRequest,
                                      final JobLaunchingMessageHandler jobLaunchingMessageHandler) {
        return IntegrationFlows
                .from(fileReadingMessageSource, c -> c.poller(Pollers.fixedDelay(5000)))//
                .channel(inputChannel)
                .transform(fileMessageToJobRequest)
                .handle(jobLaunchingMessageHandler)
                .handle(jobExecution -> System.out.println(jobExecution.getPayload()))
                .get();
    }

    @Bean
    public MessageSource<File> fileReadingMessageSource() {
        final FileReadingMessageSource source = new FileReadingMessageSource();
        source.setDirectory(new File("data"));
        source.setFilter(new SimplePatternFileListFilter("*.xml"));
        source.setUseWatchService(true);
        source.setWatchEvents(FileReadingMessageSource.WatchEventType.CREATE);
        return source;
    }

    @Bean
    public FileMessageToJobRequest fileMessageToJobRequest() {
        final FileMessageToJobRequest transformer = new FileMessageToJobRequest();
        transformer.setJob(this.sampleJob);
        transformer.setFileParameterName("file_path");
        return transformer;
    }

    @Bean
    public JobLaunchingMessageHandler jobLaunchingMessageHandler() {
        return new JobLaunchingMessageHandler(this.jobLauncher);
    }

    @Bean
    public JobLaunchingGateway jobLaunchingGateway() {
        return new JobLaunchingGateway(this.jobLauncher);
    }
}
