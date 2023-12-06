package com.example.config;

import com.example.domain.PersonEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private final JdbcTemplate jdbcTemplate;
    private final JpaRepository<PersonEntity,Long> jpaRepository;

    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate,JpaRepository<PersonEntity,Long> jpaRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results");

            List<PersonEntity> list = jdbcTemplate
                    .query("SELECT * FROM people", new DataClassRowMapper<>(PersonEntity.class));
            list.forEach(person -> {
                log.info("Found <{}> in the database.", person);
            });

            List<PersonEntity> list2 = jpaRepository.findAll();

            list2.forEach(person -> {
                log.info("Found <{}> in the database.", person);
            });
        }
    }
}
