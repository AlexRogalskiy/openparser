package ru.gkomega.api.openparser.solr.configuration;

import org.apache.solr.client.solrj.SolrClient;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Role;
import org.springframework.core.io.Resource;
import org.springframework.data.solr.core.SolrTemplate;
import ru.gkomega.api.openparser.solr.handler.MarkdownDocumentOptimizeTasklet;
import ru.gkomega.api.openparser.solr.handler.MarkdownDocumentProcessor;
import ru.gkomega.api.openparser.solr.handler.MarkdownDocumentReader;
import ru.gkomega.api.openparser.solr.handler.MarkdownDocumentWriter;
import ru.gkomega.api.openparser.solr.model.domain.HtmlResource;
import ru.gkomega.api.openparser.solr.property.MarkdownSolrResourceProperty;

@Configuration
@EnableConfigurationProperties(MarkdownSolrResourceProperty.class)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Description("OpenParser Markdown Solr Batch configuration")
public class MarkdownSolrBatchConfiguration {

    @Bean
    public Job indexMarkdownDocumentsJob(final JobBuilderFactory jobBuilderFactory,
                                         final Step indexingStep,
                                         final Step optimizeStep) {
        return jobBuilderFactory.get("indexMarkdownDocuments")
            .incrementer(new RunIdIncrementer())
            .flow(indexingStep)
            .next(optimizeStep)
            .end()
            .build();
    }

    @Bean
    public Step indexingStep(final StepBuilderFactory stepBuilderFactory,
                             final MarkdownDocumentReader reader,
                             final MarkdownDocumentProcessor processor,
                             final MarkdownDocumentWriter writer) {
        return stepBuilderFactory.get("indexingStep")
            .<Resource, HtmlResource>chunk(10)
            .reader(reader)
            .processor(processor)
            .writer(writer)
            .build();
    }

    @Bean
    public Step optimizeStep(final StepBuilderFactory stepBuilderFactory,
                             final MarkdownDocumentOptimizeTasklet tasklet) {
        return stepBuilderFactory.get("optimizeStep")
            .tasklet(tasklet)
            .build();
    }

    @Bean
    public SolrTemplate solrTemplate(final SolrClient solrClient) {
        return new SolrTemplate(solrClient);
    }
}
