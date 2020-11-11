package ru.gkomega.api.openparser.batch.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.lang.NonNull;

import static org.apache.commons.lang3.time.DurationFormatUtils.formatDurationHMS;

@Slf4j
public class ElapsedTimeJobListener implements JobExecutionListener {

    /**
     * Default stop watch instance
     */
    private final StopWatch stopWatch = new StopWatch();

    public void beforeJob(@NonNull final JobExecution jobExecution) {
        log.debug(">>> Called [beforeJob] operation");
        this.stopWatch.start();
    }

    public void afterJob(@NonNull final JobExecution jobExecution) {
        log.debug(">>> Called [afterJob] operation");
        this.stopWatch.stop();
        log.info("Elapsed time: {}", formatDurationHMS(this.stopWatch.getTime()));
    }
}
