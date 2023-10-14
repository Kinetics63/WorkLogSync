package com.entimo.worklogsync.config;

import com.entimo.worklogsync.oracle.data.KstGruppe;
import com.entimo.worklogsync.oracle.data.Project;
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
    basePackageClasses = { Project.class, KstGruppe.class},
    entityManagerFactoryRef = "pepEntityManagerFactory",
    transactionManagerRef = "pepTransactionManager"
)
public class PepJpaConfiguration {

  @Bean
  public LocalContainerEntityManagerFactoryBean pepEntityManagerFactory(
      @Qualifier("pepDataSource") DataSource dataSource,
      EntityManagerFactoryBuilder builder) {
    return builder
        .dataSource(dataSource)
        .packages(Project.class, KstGruppe.class)
        .build();
  }

  @Bean
  public PlatformTransactionManager pepTransactionManager(
      @Qualifier("pepEntityManagerFactory") LocalContainerEntityManagerFactoryBean pepEntityManagerFactory) {
    return new JpaTransactionManager(Objects.requireNonNull(pepEntityManagerFactory.getObject()));
  }
}
