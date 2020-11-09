package ru.gkomega.api.openparser.batch.logging;

import org.slf4j.MDC;
import org.springframework.batch.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * This listener writes the job log file name to the MDC so that it can be picked up by the logging framework for
 * logging to it. It's a {@link JobExecutionListener} and a {@link StepExecutionListener} because in partitioning we may
 * have a lot of {@link StepExecution}s running in different threads. Due to the fact that the afterStep - method would
 * remove the variable from the MDC in single threaded execution we need to re-set it, that's what's the
 * {@link JobExecutionListener} is for. Note that, of the three local parallelization features in Spring Batch, log
 * file separation only works for partitioning and parallel step, not for multi-threaded step.
 * <p>
 * The log file name is determined by a {@link JobLogFileNameCreator}. It's default implementation
 * {@link DefaultJobLogFileNameCreator} is used when there's no other bean of this type in the ApplicationContext.
 */
@Component
public class LoggingListener implements JobExecutionListener, StepExecutionListener, Ordered {
    /**
     * Default {@link JobLogFileNameCreator} instance
     */
    private JobLogFileNameCreator jobLogFileNameCreator = new DefaultJobLogFileNameCreator();
    /**
     * Default {@link String} job log name
     */
    private static final String JOB_LOG_FILENAME = "jobLogFileName";

    @Override
    public void beforeJob(@NonNull final JobExecution jobExecution) {
        this.insertValuesIntoMDC(jobExecution);
    }

    private void insertValuesIntoMDC(final JobExecution jobExecution) {
        MDC.put(JOB_LOG_FILENAME, this.jobLogFileNameCreator.getBaseName(jobExecution));
    }

    @Override
    public void afterJob(@NonNull final JobExecution jobExecution) {
        this.removeValuesFromMDC();
    }

    private void removeValuesFromMDC() {
        MDC.remove(JOB_LOG_FILENAME);
    }

    @Override
    public void beforeStep(@NonNull final StepExecution stepExecution) {
        this.insertValuesIntoMDC(stepExecution.getJobExecution());
    }

    @Override
    public ExitStatus afterStep(@NonNull final StepExecution stepExecution) {
        this.removeValuesFromMDC();
        return null;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Autowired(required = false)
    public void setJobLogFileNameCreator(final JobLogFileNameCreator jobLogFileNameCreator) {
        this.jobLogFileNameCreator = jobLogFileNameCreator;
    }
}
