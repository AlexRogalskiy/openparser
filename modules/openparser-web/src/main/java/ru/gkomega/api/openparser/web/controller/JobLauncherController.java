package ru.gkomega.api.openparser.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class JobLauncherController {

    private final JobLauncher jobLauncher;
    @Qualifier("dataLoaderJob")
    private final Job job;

    @GetMapping("/launchjob")
    public String handle(@RequestParam(value = "enable", required = false) final boolean enableParams) {
        try {
            final JobParametersBuilder parametersBuilder = new JobParametersBuilder();
            if (enableParams) {
                parametersBuilder.addLong("time", System.currentTimeMillis());
            }
            this.jobLauncher.run(this.job, parametersBuilder.toJobParameters());
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return "Done";
    }
}
