package ru.gkomega.api.openparser.file.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.lang.NonNull;

import java.io.File;
import java.util.Collections;
import java.util.Map;

@Slf4j
public class CleanupFileTasklet implements Tasklet {

    @Override
    public RepeatStatus execute(@NonNull final StepContribution contribution,
                                final ChunkContext chunkContext) throws Exception {
        log.debug(">>> Cleaning up the target file");

        final Map<String, Object> jobParameters = chunkContext.getStepContext().getJobParameters();
        final String filePath = (String) jobParameters.get("WRITE_FILE");

        FileUtils.writeLines(new File((filePath)), Collections.EMPTY_LIST);

        return RepeatStatus.FINISHED;
    }
}
