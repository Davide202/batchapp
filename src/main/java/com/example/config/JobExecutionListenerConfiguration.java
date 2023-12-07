package com.example.config;

import com.example.domain.PersonEntity;
import com.example.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JobExecutionListenerConfiguration implements JobExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(JobExecutionListenerConfiguration.class);

    public JobParameters jobParameters = new JobParameters();
    public Map<String,Object> mapParameters = new HashMap<>();
    private final JdbcTemplate jdbcTemplate;
    private final JpaRepository<PersonEntity,Long> jpaRepository;

    public JobExecutionListenerConfiguration(JdbcTemplate jdbcTemplate, JpaRepository<PersonEntity,Long> jpaRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.jpaRepository = jpaRepository;
    }


    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("!!! JOB STARTING! ");

        BatchStatus batchStatus = jobExecution.getStatus();
        jobParameters = jobExecution.getJobParameters();


        if (jobParameters.getParameters().containsKey("time")){
            Long objtime = (Long) jobParameters.getParameters().get("time").getValue();
            Date time = new Date(objtime);
            log.info(time.toString());
        }
        if (jobParameters.getParameters().containsKey("key")){
            String value = jobParameters.getString("key");
            log.info(value);
        }
        if (jobParameters.getParameters().containsKey("json")){
            String value = jobParameters.getString("json");
            mapParameters = JsonUtil.convertStringToMap(value);
            log.info(value);
            log.info(value);
        }

        log.info(jobParameters.toString());
//        List<PersonEntity> list = jdbcTemplate
//                .query("SELECT * FROM people", new DataClassRowMapper<>(PersonEntity.class));
//        list.forEach(person -> {
//            log.info("Found <{}> in the database.", person);
//        });

//        List<PersonEntity> list2 = jpaRepository.findAll();
//
//        list2.forEach(person -> {
//            log.info("Found <{}> in the database.", person);
//        });

    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results");

//            List<PersonEntity> list = jdbcTemplate
//                    .query("SELECT * FROM people", new DataClassRowMapper<>(PersonEntity.class));
//            list.forEach(person -> {
//                log.info("Found <{}> in the database.", person);
//            });

            List<PersonEntity> list2 = jpaRepository.findAll();

            list2.forEach(person -> {
                log.info("Found <{}> in the database.", person.toString());
            });
        }
    }
}
