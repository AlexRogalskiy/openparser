package ru.gkomega.api.openparser.executor.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration;
import org.springframework.boot.task.TaskSchedulerBuilder;
import org.springframework.boot.task.TaskSchedulerCustomizer;
import org.springframework.context.annotation.*;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.TaskUtils;
import ru.gkomega.api.openparser.executor.handler.CustomRejectedExecutionHandler;

import java.util.concurrent.RejectedExecutionHandler;

@Configuration
@AutoConfigureAfter(TaskSchedulingAutoConfiguration.class)
@ConditionalOnProperty(prefix = "spring.task.scheduling", value = "enabled", havingValue = "true", matchIfMissing = true)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Description("OpenParser Task Scheduling configuration")
public abstract class TaskSchedulerConfiguration {
    /**
     * Default thread pool task scheduler bean naming convention
     */
    public static final String THREAD_POOL_TASK_SCHEDULER_BEAN_NAME = "threadPoolTaskScheduler";
    /**
     * Default task scheduler bean naming convention
     */
    public static final String TASK_SCHEDULER_BUILDER_BEAN_NAME = "taskSchedulerBuilder";
    /**
     * Default task scheduler properties customizer bean naming convention
     */
    public static final String TASK_SCHEDULER_PROPERTIES_CUSTOMIZER_BEAN_NAME = "taskSchedulerPropertiesCustomizer";
    /**
     * Default task scheduler handler customizer bean naming convention
     */
    public static final String TASK_SCHEDULER_HANDLER_CUSTOMIZER_BEAN_NAME = "taskSchedulerHandlerCustomizer";

    @Bean(name = THREAD_POOL_TASK_SCHEDULER_BEAN_NAME, destroyMethod = "shutdown")
    @ConditionalOnMissingBean
    @DependsOn(TASK_SCHEDULER_BUILDER_BEAN_NAME)
    @Description("Default thread pool task scheduler configuration bean")
    public ThreadPoolTaskScheduler threadPoolTaskScheduler(final TaskSchedulerBuilder taskSchedulerBuilder) {
        final ThreadPoolTaskScheduler taskScheduler = taskSchedulerBuilder.build();
        taskScheduler.afterPropertiesSet();
        return taskScheduler;
    }

    @Bean(TASK_SCHEDULER_PROPERTIES_CUSTOMIZER_BEAN_NAME)
    @ConditionalOnMissingBean(name = TASK_SCHEDULER_PROPERTIES_CUSTOMIZER_BEAN_NAME)
    @Description("Default thread pool task scheduler properties customizer bean")
    public TaskSchedulerCustomizer propertiesCustomizer() {
        return taskScheduler -> {
            taskScheduler.setWaitForTasksToCompleteOnShutdown(true);
            taskScheduler.setRemoveOnCancelPolicy(true);
        };
    }

    @Bean(TASK_SCHEDULER_HANDLER_CUSTOMIZER_BEAN_NAME)
    @ConditionalOnBean(RejectedExecutionHandler.class)
    @ConditionalOnMissingBean(name = TASK_SCHEDULER_HANDLER_CUSTOMIZER_BEAN_NAME)
    @Description("Default thread pool task scheduler handler customizer bean")
    public TaskSchedulerCustomizer handlerCustomizer(final RejectedExecutionHandler rejectedExecutionHandler) {
        return taskScheduler -> {
            taskScheduler.setRejectedExecutionHandler(rejectedExecutionHandler);
            taskScheduler.setErrorHandler(TaskUtils.LOG_AND_PROPAGATE_ERROR_HANDLER);
        };
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(CustomRejectedExecutionHandler.class)
    @Description("Default rejected execution handler bean")
    public RejectedExecutionHandler rejectedExecutionHandler() {
        return new CustomRejectedExecutionHandler();
    }

    @Configuration
    @RequiredArgsConstructor
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Description("SensibleMetrics WebService Task Scheduling configuration")
    public static class WsTaskSchedulingConfigurer implements SchedulingConfigurer {
        private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

        /**
         * {@inheritDoc}
         *
         * @see SchedulingConfigurer
         */
        @Override
        public void configureTasks(@NonNull final ScheduledTaskRegistrar taskRegistrar) {
            taskRegistrar.setScheduler(this.threadPoolTaskScheduler);
        }
    }
}
