package ru.gkomega.api.openparser.solr.handler;

import lombok.RequiredArgsConstructor;
import org.apache.solr.client.solrj.SolrClient;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.gkomega.api.openparser.solr.model.entity.MarkdownDocumentEntity;

@Component
@RequiredArgsConstructor
public class MarkdownDocumentOptimizeTasklet implements Tasklet {

    private final SolrClient solrClient;

    @Override
    public RepeatStatus execute(@NonNull final StepContribution stepContribution,
                                @NonNull final ChunkContext chunkContext) throws Exception {
        this.solrClient.optimize(MarkdownDocumentEntity.MARKDOWN_CORE);
        return RepeatStatus.FINISHED;
    }
}
