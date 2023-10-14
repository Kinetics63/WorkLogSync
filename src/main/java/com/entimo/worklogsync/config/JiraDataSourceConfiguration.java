package com.entimo.worklogsync.config;

import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JiraDataSourceConfiguration {

  @Bean
  @ConfigurationProperties("spring.datasource.jira")
  public DataSourceProperties jiraDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Primary
  @Bean(name = "jiraDataSource")
  public DataSource jiraDataSource() {
    return jiraDataSourceProperties()
        .initializeDataSourceBuilder()
        .build();
  }
}
