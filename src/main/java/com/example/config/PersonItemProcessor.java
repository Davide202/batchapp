package com.example.config;

import com.example.domain.Person;
import com.example.domain.PersonEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class PersonItemProcessor implements ItemProcessor<Person, PersonEntity> {

    private static final Logger log = LoggerFactory.getLogger(PersonItemProcessor.class);

    @Override
    public PersonEntity process(final Person person) {
//        final String firstName = person.firstName().toUpperCase();
        final String firstName = person.getFirstName().toUpperCase();
        final String lastName = person.getLastName().toUpperCase();
        final Integer age = person.getAge() == null ? null :
                Integer.valueOf(person.getAge())
                ;

        final PersonEntity transformedPerson = new PersonEntity(firstName, lastName, age);

        log.info("Converting ({}) into ({})",person,transformedPerson);
//        System.out.println("Converting (" + person + ") into (" + transformedPerson + ")");

        return transformedPerson;
    }

}