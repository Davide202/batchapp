package com.example.repository;

import com.example.domain.PersonEntity;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class JdbcPersonEntityWriter {

    @Bean
    public JdbcBatchItemWriter<PersonEntity> writer(
            DataSource dataSource
    ) {
        return new JdbcBatchItemWriterBuilder<PersonEntity>()
                .sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)")
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }
}
