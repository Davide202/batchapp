package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class BatchappApplication {
	//    https://spring.io/guides/gs/batch-processing/
//    https://docs.spring.io/spring-batch/reference/index.html
	public static void main(String[] args) {
		SpringApplication.run(BatchappApplication.class, args);

//		System.exit(
//				SpringApplication.exit(
//						SpringApplication.run(BatchappApplication.class, args)
//				)
//		);


	}



}
