package ru.gkomega.api.openparser.db;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.gkomega.api.openparser.batch.annotation.EnableBatch;
import ru.gkomega.api.openparser.executor.annotation.EnableTaskExecutor;
import ru.gkomega.api.openparser.web.annotation.EnableWeb;
import ru.gkomega.api.openparser.xstream.annotation.EnableXStream;

@EnableWeb
@EnableBatch
@EnableXStream
@EnableTaskExecutor
@EnableConfigurationProperties
@SpringBootApplication
public class ApplicationBootstrap {

    public static void main(final String[] args) {
        SpringApplication.run(ApplicationBootstrap.class, args);
    }
}
