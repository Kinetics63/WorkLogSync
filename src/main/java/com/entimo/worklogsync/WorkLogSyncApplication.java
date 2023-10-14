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

@SpringBootApplication
@Slf4j
public class WorkLogSyncApplication  implements InitializingBean, DisposableBean {

    public static void main(String[] args) {

      SpringApplication.run(WorkLogSyncApplication.class, args);

    }

  @Override
  public void destroy() throws Exception {
    log.info("destroy application");
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    log.info("after properties set");
  }
}
