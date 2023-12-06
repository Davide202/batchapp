package com.example.batchprocessing;

import com.example.config.JobCompletionNotificationListener;
import com.example.config.PersonItemProcessor;
import com.example.domain.Person;
import com.example.domain.PersonEntity;
import com.example.repository.JpaPersonEntityWriter;
import lombok.SneakyThrows;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.springframework.jdbc.support.JdbcTransactionManager;
import java.util.*;

@Configuration
@EnableBatchProcessing
@SuppressWarnings({"java:S2293","java:S125"})
public class JobConfiguration {
    private Logger log = LoggerFactory.getLogger(JobConfiguration.class);
    @Autowired
    JdbcTransactionManager transactionManager;
    @Autowired
    JobRepository jobRepository;



    @Bean
    @SneakyThrows
    public Job importUserJob(
            JobRepository jobRepository,
            Step step1,
            JobCompletionNotificationListener listener
    ) {

        log.info("Start Job Import User");
        Job job = new JobBuilder("importUserJob", jobRepository)
                .listener(listener)
                .start(step1)
//                .next(step1)
                .build();
//        jobLauncher.run(importUserJob,new JobParameters());
        log.info("End Job Import User");
        return job;
    }

    @Bean
    @SneakyThrows
    public Step step1(
            FlatFileItemReader<Person> reader,
            PersonItemProcessor processor,
//            JdbcBatchItemWriter<PersonEntity> writer
            JpaPersonEntityWriter writer
    ) {
        log.info("Start Step Import User");
        Step step = new StepBuilder("step1-" + UUID.randomUUID(), jobRepository)
                .<Person, PersonEntity> chunk(3, transactionManager)
                .reader(reader)
                .processor(processor)
//              .writer(new PersonEntityWriter(entityManager))
                .writer(writer)
                .build();
        log.info("End Step Import User");
        return step;
    }

}
