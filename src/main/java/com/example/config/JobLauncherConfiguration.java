package com.example.config;

import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
public class JobLauncherConfiguration {

    @Bean
    public JobLauncher asyncJobLauncher(JobRepository jobRepository) throws Exception {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor();
        simpleAsyncTaskExecutor.setConcurrencyLimit(10);
        simpleAsyncTaskExecutor.setVirtualThreads(false);//jdk21 required to set true
        simpleAsyncTaskExecutor.setThreadNamePrefix("async_thread_");
        simpleAsyncTaskExecutor.setDaemon(Boolean.TRUE);
        jobLauncher.setTaskExecutor(simpleAsyncTaskExecutor);
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }
}
