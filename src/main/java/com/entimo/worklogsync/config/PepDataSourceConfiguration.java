package com.entimo.worklogsync.config;

import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PepDataSourceConfiguration {

  @Bean
  @ConfigurationProperties("spring.datasource.pep")
  public DataSourceProperties pepDataSourceProperties() {
    return new DataSourceProperties();
  }


  @Bean
  public DataSource pepDataSource() {
    return pepDataSourceProperties()
        .initializeDataSourceBuilder()
        .build();
  }

}
