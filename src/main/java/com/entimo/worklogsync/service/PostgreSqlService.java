package com.entimo.worklogsync.service;

import com.entimo.worklogsync.postgresql.data.JiraIssue;
import com.entimo.worklogsync.postgresql.data.JiraIssueRepository;
import com.entimo.worklogsync.postgresql.data.WorkLog;
import com.entimo.worklogsync.postgresql.data.WorkLogRepository;

import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.List;

import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class PostgreSqlService {

    private WorkLogRepository worklogRepro;
    private JiraIssueRepository issueRepro;

    public PostgreSqlService(WorkLogRepository worklogRepro,JiraIssueRepository issueRepro) {
        this.worklogRepro = worklogRepro;
        this.issueRepro = issueRepro;
    }

    public int loadWorkLog(Integer lastDays) {
        List<WorkLog> rw = this.worklogRepro.findByCreationDate(ZonedDateTime.now().minusDays(lastDays));
        rw.forEach(w->loadIssue(w.getIssueid()));
        return rw.size();
    }

    private void loadIssue(Long issueid) {
        Optional<JiraIssue> issueOpt = issueRepro.findById(issueid);
        if(issueOpt.isPresent()){
            log.info(issueOpt.get().toString());
        }

    }
}
