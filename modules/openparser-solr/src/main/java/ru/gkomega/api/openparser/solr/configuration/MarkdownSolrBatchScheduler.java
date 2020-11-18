package ru.gkomega.api.openparser.solr.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class MarkdownSolrBatchScheduler {

    private final JobLauncher jobLauncher;
    private final Job indexMarkdownDocumentsJob;

    @Scheduled(cron = "${openparser.batch.cron}")
    public void schedule() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        this.jobLauncher.run(this.indexMarkdownDocumentsJob, new JobParametersBuilder()
            .addDate("date", new Date())
            .toJobParameters()
        );
    }
}
