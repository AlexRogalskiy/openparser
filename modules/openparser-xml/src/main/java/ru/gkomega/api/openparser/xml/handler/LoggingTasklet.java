package ru.gkomega.api.openparser.xml.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.util.StringUtils.collectionToCommaDelimitedString;

@Slf4j
@Component
public class LoggingTasklet implements Tasklet {

    @Override
    public RepeatStatus execute(@NonNull final StepContribution stepContribution,
                                @NonNull final ChunkContext chunkContext) {
        log.error(
            ">>> Logging data step: [{}]\n\n job: [{}]\n params: [{}]\n",
            chunkContext.getStepContext().getStepName(),
            chunkContext.getStepContext().getJobName(),
            collectionToCommaDelimitedString(chunkContext.getStepContext().getJobParameters().entrySet())
        );
        return RepeatStatus.FINISHED;
    }
}
