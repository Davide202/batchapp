package com.example.batchprocessing;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableScheduling
public class ScheduledJob {
    private Logger log = LoggerFactory.getLogger(ScheduledJob.class);
    @Autowired
    Job importUserJob;
    @Autowired
    JobLauncher jobLauncher;
    @Autowired
    JobLauncher asyncJobLauncher;
    @Autowired
    JobRepository jobRepository;

    @Scheduled(
            timeUnit = TimeUnit.SECONDS,
            initialDelay = 20,
            fixedDelay = 200
    )
    @SneakyThrows
    public void runScheduled() {
        log.info("\n *** JOB RUN FROM SCHEDULER **** \n");
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("key","value1")
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        JobExecution jobExecution = asyncJobLauncher.run(importUserJob,jobParameters);
        log.info("Created {}",jobExecution);
    }

    @Bean
    @SneakyThrows
    @DependsOn({"importUserJob"})
    public List<JobExecution> runJobs(){
//        deleteLastStepExecution();
        List<JobExecution> list = new ArrayList<>();
        list.add(runImportUserJob());
        //add other jobs here
        return list;
    }

    @SneakyThrows
    private JobExecution runImportUserJob(){
        log.info("\n *** JOB RUN AT START APPLICATION **** \n");
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("key","value2")
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        JobExecution jobExecution = asyncJobLauncher.run(importUserJob,jobParameters);
        log.info("Created {}",jobExecution);
        return jobExecution;
    }
    @Transactional
    private void deleteLastStepExecution(){
        jobRepository.findJobInstancesByName("importUserJob",0,2).stream().findFirst()
                .ifPresent(jobInstance -> {
                    Optional.ofNullable(jobRepository.getLastStepExecution(jobInstance,"step1"))
                            .ifPresent(stepExecution -> {
                                log.info("Deleting {}",stepExecution);
                                jobRepository.deleteStepExecution(stepExecution);
                            });

                });
    }
}
