package com.entimo.worklogsync;

import com.entimo.worklogsync.postgresql.data.WorkLog;
import com.entimo.worklogsync.postgresql.data.WorkLogRepository;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
@Slf4j
public class WorkLogSyncApplication implements InitializingBean, DisposableBean {

  private static Environment env;

  public WorkLogSyncApplication(Environment env) {
    this.env = env;
  }

  public static void main(String[] args) {

    ConfigurableApplicationContext ctx = SpringApplication.run(WorkLogSyncApplication.class,
        args);

    String shutdownAfterSync = env.getProperty("shutdownAfterSync");
    if (Boolean.parseBoolean(shutdownAfterSync)) {
      log.info("System shutdown");
      ctx.close();
      ctx.stop();
      System.exit(0);
    }
  }

  @Override
  public void destroy() throws Exception {
//    log.info("destroy application");
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    //   log.info("after properties set");
  }
}
