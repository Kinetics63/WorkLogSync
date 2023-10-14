package com.entimo.worklogsync.config;

import com.entimo.worklogsync.postgresql.data.WorkLog;

import java.util.Objects;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackageClasses = WorkLog.class,
        entityManagerFactoryRef = "jiraEntityManagerFactory",
        transactionManagerRef = "jiraTransactionManager"
)
public class JiraJpaConfiguration {

    @Primary
    @Bean (name="jiraEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean jiraEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("jiraDataSource") DataSource jiraDataSource) {
        return builder
                .dataSource(jiraDataSource)
                .packages(WorkLog.class)
                .build();
    }

    @Primary
    @Bean(name ="jiraTransactionManager")
    public PlatformTransactionManager jiraTransactionManager(
            @Qualifier("jiraEntityManagerFactory") LocalContainerEntityManagerFactoryBean jiraEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(jiraEntityManagerFactory.getObject()));
    }
}
