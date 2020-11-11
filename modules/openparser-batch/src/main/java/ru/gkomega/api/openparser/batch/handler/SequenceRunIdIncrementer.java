package ru.gkomega.api.openparser.batch.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;

import java.util.Optional;

@RequiredArgsConstructor
public class SequenceRunIdIncrementer implements JobParametersIncrementer {

    private final JdbcTemplate jdbcTemplate;

    @NonNull
    @Override
    public JobParameters getNext(final JobParameters parameters) {
        final Long nextRunId = Optional.ofNullable(this.jdbcTemplate.queryForObject("select batch_run_id_seq.nextval from dual", Long.class)).orElse(0L);
        final JobParametersBuilder jobParametersBuilder = new JobParametersBuilder(parameters);
        jobParametersBuilder.addLong("run.id", nextRunId);
        return jobParametersBuilder.toJobParameters();
    }
}
