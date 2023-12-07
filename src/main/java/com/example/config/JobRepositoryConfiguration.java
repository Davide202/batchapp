package com.example.config;

import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.item.database.support.DataFieldMaxValueIncrementerFactory;
import org.springframework.batch.item.database.support.DefaultDataFieldMaxValueIncrementerFactory;
import org.springframework.batch.support.DatabaseType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.support.JdbcTransactionManager;
import javax.sql.DataSource;

@Configuration
public class JobRepositoryConfiguration {

    private DataFieldMaxValueIncrementerFactory incrementerFactory;
    public JobRepositoryConfiguration(DataSource dataSource) {
        incrementerFactory = new DefaultDataFieldMaxValueIncrementerFactory(dataSource);
    }


    @Bean
    public JobRepository createJobRepository(DataSource dataSource,JdbcTransactionManager transactionManager) throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(dataSource);
        factory.setTransactionManager(transactionManager);

        factory.setDatabaseType(DatabaseType.H2.getProductName());

        factory.setIsolationLevelForCreate("ISOLATION_REPEATABLE_READ");
        factory.setTablePrefix("BATCH_");
        factory.setMaxVarCharLength(1000);

        factory.afterPropertiesSet();
        return factory.getObject();
    }
}
