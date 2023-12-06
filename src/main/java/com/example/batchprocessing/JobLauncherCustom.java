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
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Configuration
public class JobLauncherCustom {
    private Logger log = LoggerFactory.getLogger(JobLauncherCustom.class);

    @Autowired
    Job importUserJob;

    @Autowired
    JobLauncher jobLauncher;
    @Autowired
    JobRepository jobRepository;
    @Bean
    @SneakyThrows
    @DependsOn({"importUserJob"})
    public List<JobExecution> runJobs(){
//        cleanSteps();
        List<JobExecution> list = new ArrayList<>();
        list.add(runImportUserJob());
        return list;
    }
    @SneakyThrows
    private JobExecution runImportUserJob(){
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("key","value")
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        JobExecution jobExecution = jobLauncher.run(importUserJob,jobParameters);
        log.info("Created {}",jobExecution);
        return jobExecution;
    }
    @Transactional
    private void cleanSteps(){
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
