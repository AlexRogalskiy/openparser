package ru.gkomega.api.openparser.batch.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.PassThroughLineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Role;
import org.springframework.core.io.FileSystemResource;
import ru.gkomega.api.openparser.batch.property.BatchProperty;

@Configuration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Description("OpenParser Batch Flow configuration")
public abstract class BatchFlowConfiguration {
    /**
     * Default bean naming conventions
     */
    public static final String STEP_FLOW_PROCESSOR_BEAN_NAME = "processorFlowStep";
    public static final String JOB_FLOW_PROCESSOR_BEAN_NAME = "processorFlowJob";

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean(STEP_FLOW_PROCESSOR_BEAN_NAME)
    public Step processorFlowStep(final BatchProperty batchProperty,
                                  final FlatFileItemReader<String> itemReader) {
        return this.stepBuilderFactory.get(STEP_FLOW_PROCESSOR_BEAN_NAME)
                .<String, String>chunk(batchProperty.getChunkSize())
                .reader(itemReader)
                .writer(i -> i.forEach(System.out::println))
                .build();
    }

    @Bean(JOB_FLOW_PROCESSOR_BEAN_NAME)
    public Job processorFlowJob(@Qualifier(STEP_FLOW_PROCESSOR_BEAN_NAME) final Step processorFlowStep) {
        return this.jobBuilderFactory.get(JOB_FLOW_PROCESSOR_BEAN_NAME)
                .incrementer(new RunIdIncrementer())
                .start(processorFlowStep)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<String> itemReader(@Value("#{jobParameters[file_path]}") final String filePath) {
        final FlatFileItemReader<String> reader = new FlatFileItemReader<>();
        final FileSystemResource fileResource = new FileSystemResource(filePath);
        reader.setResource(fileResource);
        reader.setLineMapper(new PassThroughLineMapper());
        return reader;
    }
}
