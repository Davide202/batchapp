package com.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.support.JdbcTransactionManager;
import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration

public class DataSourceConfiguration {


    @Value("${spring.datasource.url:jdbc:mysql://127.0.0.1:33306/test?serverTimezone=UTC}")
    private String url;

    @Value("${spring.datasource.username:}")
    private String username;

    @Value("${spring.datasource.password:}")
    private String password;

    private String filePath = "src/test/resources/schema-all.sql";

//    @Bean
//    public DataSource dataSource() {
//        DataSource dataSource = new DriverManagerDataSource(url,username,password);
//        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.HSQL)
//                .addScript("/org/springframework/batch/core/schema-hsqldb.sql")
//                .generateUniqueName(true).build();

//        try(Connection connection = dataSource.getConnection()){
//            connection.prepareCall(Files.readString(Path.of(filePath))).execute();
//            connection.commit();
//        }catch (Exception e){
//            System.out.println("**** Database Connection Error ****");
//        }
//        return dataSource;
//    }

    @Bean
    public JdbcTransactionManager transactionManager(DataSource dataSource) {
        return new JdbcTransactionManager(dataSource);
    }
}
