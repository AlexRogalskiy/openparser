package ru.gkomega.api.openparser.batch.handler;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.lang.NonNull;

import java.util.Optional;

public class StepFlowDecider implements JobExecutionDecider {

    @NonNull
    @Override
    public FlowExecutionStatus decide(final JobExecution jobExecution,
                                      final StepExecution stepExecution) {
        final String FLOW_FLAG = Optional.ofNullable(jobExecution.getJobParameters().getString("FLOW_FLAG")).orElse("EXIT");
        return new FlowExecutionStatus(FLOW_FLAG);
    }
}
