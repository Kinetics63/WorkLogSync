package com.entimo.worklogsync.service;

import com.entimo.worklogsync.postgresql.data.WorkLog;
import com.entimo.worklogsync.postgresql.data.WorkLogRepository;

import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.List;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class PostgreSqlService {

    private WorkLogRepository worklogRepro;

    public PostgreSqlService(WorkLogRepository worklogRepro) {
        this.worklogRepro = worklogRepro;
    }

    public int loadWorkLog(Integer lastDays) {
        List<WorkLog> rw = this.worklogRepro.findByCreationDate(ZonedDateTime.now().minusDays(lastDays));
        //rw.forEach(w->log.info(w.toString()));
        return rw.size();
    }
}
