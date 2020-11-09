package ru.gkomega.api.openparser.batch.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomJobListener extends JobExecutionListenerSupport {

    @Override
    public void beforeJob(final JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.STARTED) {
            log.info(">>> Batch job [{}] initialized", jobExecution.getJobInstance());
        }
    }

    @Override
    public void afterJob(final JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info(">>> Batch job [{}] completed", jobExecution.getJobInstance());
        }
    }
}
