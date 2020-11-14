package ru.gkomega.api.openparser.batch.configuration;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.io.File;
import java.util.Arrays;
import java.util.Optional;

public class FileDeletingTasklet implements Tasklet, InitializingBean {

    private Resource directory;

    public RepeatStatus execute(@NonNull final StepContribution contribution,
                                @NonNull final ChunkContext chunkContext) throws Exception {
        final File dir = this.directory.getFile();
        return Optional.ofNullable(dir.listFiles())
            .stream()
            .flatMap(Arrays::stream)
            .map(v -> {
                boolean deleted = v.delete();
                if (!deleted) {
                    throw new UnexpectedJobExecutionException("Could not delete file " + v.getPath());
                }
                return RepeatStatus.FINISHED;
            })
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Error: directory is empty: " + dir.getPath()));
    }

    public void setDirectoryResource(Resource directory) {
        this.directory = directory;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(directory, "directory must be set");
    }
}
