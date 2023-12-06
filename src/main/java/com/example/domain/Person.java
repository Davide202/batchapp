package com.example.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

//public record Person(String firstName, String lastName,String age) {
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class Person {
    String firstName;
    String lastName;
    String age;
}
