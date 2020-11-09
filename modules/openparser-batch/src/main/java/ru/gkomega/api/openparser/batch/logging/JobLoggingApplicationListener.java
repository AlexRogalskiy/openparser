package ru.gkomega.api.openparser.batch.logging;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;

import java.io.File;

/**
 * This ApplicationListener makes the batch.joblog.path available before the LoggingSystem is started.
 */
public class JobLoggingApplicationListener implements ApplicationListener<ApplicationPreparedEvent>, Ordered {
    /**
     * {@inheritDoc}
     *
     * @see ApplicationListener
     */
    @Override
    public void onApplicationEvent(@NonNull final ApplicationPreparedEvent event) {
        String jobLogPath = event.getApplicationContext().getEnvironment().getProperty("openparser.batch.log.path");
        if (!StringUtils.isEmpty(jobLogPath)) {
            if (!jobLogPath.endsWith(File.separator)) {
                jobLogPath = jobLogPath + File.separator;
            }
            System.setProperty("JOB_LOG_PATH", jobLogPath);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see ApplicationListener
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 11;
    }
}
