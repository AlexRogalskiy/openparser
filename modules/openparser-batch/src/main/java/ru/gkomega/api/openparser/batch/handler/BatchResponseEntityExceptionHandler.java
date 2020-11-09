package ru.gkomega.api.openparser.batch.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobParametersNotFoundException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.batch.operations.JobExecutionAlreadyCompleteException;
import javax.batch.operations.JobStartException;
import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;

import static ru.gkomega.api.openparser.commons.configuration.ExceptionModelConfigurer.buildResponseEntity;

@Slf4j
@RestControllerAdvice
@Description("Default batch response entity exception handler")
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class BatchResponseEntityExceptionHandler {

    @ExceptionHandler({
            NoSuchJobException.class,
            NoSuchJobExecutionException.class,
            JobStartException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleNotFound(final HttpServletRequest request,
                                            final Exception exception) {
        this.logMessage(request.getServletPath(), exception);
        return buildResponseEntity(request.getServletPath(), exception.getLocalizedMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(JobParametersNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleNoBootstrapParametersCreatedByIncrementer(final HttpServletRequest request,
                                                                             final Exception exception) {
        this.logMessage(request.getServletPath(), exception);
        return buildResponseEntity(request.getServletPath(), exception.getLocalizedMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            UnexpectedJobExecutionException.class,
            JobInstanceAlreadyExistsException.class,
            JobInstanceAlreadyCompleteException.class
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<?> handleAlreadyExists(final HttpServletRequest request,
                                                 final Exception exception) {
        this.logMessage(request.getServletPath(), exception);
        return buildResponseEntity(request.getServletPath(), exception.getLocalizedMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({
            JobExecutionAlreadyRunningException.class,
            JobExecutionAlreadyCompleteException.class,
            JobRestartException.class
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<?> handleAlreadyRunningOrComplete(final HttpServletRequest request,
                                                            final Exception exception) {
        this.logMessage(request.getServletPath(), exception);
        return buildResponseEntity(request.getServletPath(), exception.getLocalizedMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(JobParametersInvalidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<?> handleParametersInvalid(final HttpServletRequest request,
                                                     final Exception exception) {
        this.logMessage(request.getServletPath(), exception);
        return buildResponseEntity(request.getServletPath(), exception.getLocalizedMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(FileNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleFileNotFound(final HttpServletRequest request,
                                                final Exception exception) {
        this.logMessage(request.getServletPath(), exception);
        return buildResponseEntity(request.getServletPath(), exception.getLocalizedMessage(), HttpStatus.NOT_FOUND);
    }

    protected void logMessage(final String path, final Exception exception) {
        log.error(">>> Handling error >>> [URI: {}]" + "[message: {}]", path, exception.getLocalizedMessage());
    }
}
