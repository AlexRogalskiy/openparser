package ru.gkomega.api.openparser.executor.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.boot.task.TaskExecutorCustomizer;
import org.springframework.context.annotation.*;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.support.TaskUtils;
import org.springframework.web.context.request.async.CallableProcessingInterceptor;
import ru.gkomega.api.openparser.executor.handler.CustomAsyncUncaughtExceptionHandler;
import ru.gkomega.api.openparser.executor.handler.CustomRejectedExecutionHandler;
import ru.gkomega.api.openparser.executor.handler.CustomTimeoutCallableProcessingInterceptor;

import java.util.concurrent.RejectedExecutionHandler;

import static org.springframework.context.support.AbstractApplicationContext.APPLICATION_EVENT_MULTICASTER_BEAN_NAME;
import static ru.gkomega.api.openparser.executor.configuration.TaskExecutorConfiguration.AsyncTaskExecutorConfiguration.ASYNC_TASK_EXECUTOR_BEAN_NAME;

@Configuration
@Import(TaskExecutionAutoConfiguration.class)
@ConditionalOnProperty(prefix = "spring.task.execution", value = "enabled", havingValue = "true", matchIfMissing = true)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Description("OpenParser Executor configuration")
public abstract class TaskExecutorConfiguration {
    /**
     * Default executor bean naming convention
     */
    public static final String TASK_EXECUTOR_BUILDER_BEAN_NAME = "taskExecutorBuilder";
    public static final String TASK_EXECUTOR_PROPERTIES_CUSTOMIZER_BEAN_NAME = "taskExecutorPropertiesCustomizer";
    public static final String TASK_EXECUTOR_HANDLER_CUSTOMIZER_BEAN_NAME = "taskExecutorHandlerCustomizer";

    @Configuration
    @RequiredArgsConstructor
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Description("OpenParser Async Task Executor configuration")
    public static class AsyncTaskExecutorConfiguration implements AsyncConfigurer {
        /**
         * Default async task executor bean naming convention
         */
        public static final String ASYNC_TASK_EXECUTOR_BEAN_NAME = "asyncTaskExecutor";

        private final ThreadPoolTaskExecutor threadPoolTaskExecutor;
        private final AsyncUncaughtExceptionHandler asyncUncaughtExceptionHandler;

        /**
         * {@inheritDoc}
         *
         * @see AsyncConfigurer
         */
        @Override
        public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
            return this.asyncUncaughtExceptionHandler;
        }

        /**
         * {@inheritDoc}
         *
         * @see AsyncConfigurer
         */
        @Override
        @Bean(ASYNC_TASK_EXECUTOR_BEAN_NAME)
        @Description("Asynchronous task executor bean")
        public AsyncTaskExecutor getAsyncExecutor() {
            return new TaskExecutorAdapter(this.threadPoolTaskExecutor);
        }
    }

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean
    @DependsOn(TASK_EXECUTOR_BUILDER_BEAN_NAME)
    @Description("Thread pool task executor configuration bean")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(final TaskExecutorBuilder taskExecutorBuilder,
                                                         final RejectedExecutionHandler rejectedExecutionHandler) {
        final ThreadPoolTaskExecutor taskExecutor = taskExecutorBuilder.build();
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.setAllowCoreThreadTimeOut(true);
        taskExecutor.setRejectedExecutionHandler(rejectedExecutionHandler);
        taskExecutor.afterPropertiesSet();
        return taskExecutor;
    }

    @Bean(TASK_EXECUTOR_PROPERTIES_CUSTOMIZER_BEAN_NAME)
    @ConditionalOnMissingBean(name = TASK_EXECUTOR_PROPERTIES_CUSTOMIZER_BEAN_NAME)
    @Description("Thread pool task executor properties customizer bean")
    public TaskExecutorCustomizer propertiesCustomizer() {
        return taskExecutor -> {
            taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
            taskExecutor.setAllowCoreThreadTimeOut(true);
        };
    }

    @Bean(TASK_EXECUTOR_HANDLER_CUSTOMIZER_BEAN_NAME)
    @ConditionalOnMissingBean(name = TASK_EXECUTOR_HANDLER_CUSTOMIZER_BEAN_NAME)
    @ConditionalOnBean(RejectedExecutionHandler.class)
    @Description("Thread pool task executor handler customizer bean")
    public TaskExecutorCustomizer handlerCustomizer(final RejectedExecutionHandler rejectedExecutionHandler) {
        return taskExecutor -> taskExecutor.setRejectedExecutionHandler(rejectedExecutionHandler);
    }

    @Bean(APPLICATION_EVENT_MULTICASTER_BEAN_NAME)
    @ConditionalOnMissingBean
    @ConditionalOnClass(SimpleApplicationEventMulticaster.class)
    @ConditionalOnBean(TaskExecutor.class)
    @Description("Application event multicaster bean")
    public ApplicationEventMulticaster simpleApplicationEventMulticaster(@Qualifier(ASYNC_TASK_EXECUTOR_BEAN_NAME) final TaskExecutor taskExecutor) {
        final SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();
        eventMulticaster.setTaskExecutor(taskExecutor);
        eventMulticaster.setErrorHandler(TaskUtils.LOG_AND_PROPAGATE_ERROR_HANDLER);
        return eventMulticaster;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(CustomTimeoutCallableProcessingInterceptor.class)
    @Description("Custom callable processing interceptor configuration bean")
    public CallableProcessingInterceptor callableProcessingInterceptor() {
        return new CustomTimeoutCallableProcessingInterceptor();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(CustomRejectedExecutionHandler.class)
    @Description("Custom rejected execution handler configuration bean")
    public RejectedExecutionHandler rejectedExecutionHandler() {
        return new CustomRejectedExecutionHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(CustomAsyncUncaughtExceptionHandler.class)
    @Description("Custom asynchronous uncaught exception handler bean")
    public AsyncUncaughtExceptionHandler asyncUncaughtExceptionHandler() {
        return new CustomAsyncUncaughtExceptionHandler();
    }
}
