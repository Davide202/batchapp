package com.example.batchprocessing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRunner implements CommandLineRunner {
    private Logger log = LoggerFactory.getLogger(ApplicationRunner.class);
    @Autowired
    JobLauncher jobLauncher;
    @Autowired
    JobLauncher asyncJobLauncher;
    @Autowired
    Job importUserJob;

    @Override
    public void run(String... args) throws Exception {

        log.info("\n *** JOB RUN FROM COMMAND LINE RUNNER **** \n");
        JobParameters parameters = new JobParametersBuilder()
                .addString("json",args[0])
                .toJobParameters();
        JobExecution execution = asyncJobLauncher.run(importUserJob,parameters);
        log.info(execution.toString());

    }
}
