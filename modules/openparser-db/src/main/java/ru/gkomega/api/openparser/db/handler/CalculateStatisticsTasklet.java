package ru.gkomega.api.openparser.db.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CalculateStatisticsTasklet implements Tasklet {

    @Override
    public RepeatStatus execute(@NonNull final StepContribution stepContribution,
                                @NonNull final ChunkContext chunkContext) {
       log.info("[{}] contains data", chunkContext.getStepContext().getJobName());
        return RepeatStatus.FINISHED;
    }
}
