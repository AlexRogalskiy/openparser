package ru.gkomega.api.openparser.batch.logging;

import org.springframework.batch.core.JobExecution;

/**
 * Default implementation used when there's no other Spring bean implementing {@link JobLogFileNameCreator} in the
 * ApplicationContext.
 */
public class DefaultJobLogFileNameCreator implements JobLogFileNameCreator {
    /**
     * Default {@link String} log extension
     */
    private static final String DEFAULT_EXTENSION = ".log";

    @Override
    public String getName(final JobExecution jobExecution) {
        return this.getBaseName(jobExecution) + this.getExtension();
    }

    @Override
    public String getBaseName(final JobExecution jobExecution) {
        return "batch-" + jobExecution.getJobInstance().getJobName() + "-" + jobExecution.getId();
    }

    @Override
    public String getExtension() {
        return DEFAULT_EXTENSION;
    }
}
