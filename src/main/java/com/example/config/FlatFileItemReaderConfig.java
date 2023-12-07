package com.example.config;

import com.example.domain.Person;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
//@StepScope
public class FlatFileItemReaderConfig {

//    @Value("#{jobParameters}") Map<String,Object> jobParameters;

    @Bean
    public FlatFileItemReader<Person> reader() {

        return new FlatFileItemReaderBuilder<Person>()
                .name("personItemReader")
                .resource(new ClassPathResource("sample-data.csv"))
                .delimited()
                .names(
                        Person.Fields.firstName,
                        Person.Fields.lastName,
                        Person.Fields.age
                        )
                .targetType(Person.class)
                .build();
    }
}
