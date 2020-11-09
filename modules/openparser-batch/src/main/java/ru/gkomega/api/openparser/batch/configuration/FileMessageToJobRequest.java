package ru.gkomega.api.openparser.batch.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;

import java.io.File;
import java.util.Date;

/**
 * File message to job request mapper
 * http://docs.spring.io/spring-batch/trunk/reference/html/springBatchIntegration.html#launching-batch-jobs-through-messages
 */
@Getter
@Setter
public class FileMessageToJobRequest {

    private Job job;
    private String fileParameterName = "input.file.name";

    @Transformer
    public JobLaunchRequest toRequest(final Message<File> message) {
        final JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addString(this.fileParameterName, message.getPayload().getAbsolutePath());
        jobParametersBuilder.addDate("dummy", new Date());

        return new JobLaunchRequest(this.job, jobParametersBuilder.toJobParameters());
    }
}
