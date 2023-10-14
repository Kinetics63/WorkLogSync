package com.entimo.worklogsync.service;

import com.entimo.worklogsync.postgresql.data.*;

import java.time.ZonedDateTime;
import java.util.List;

import java.util.Optional;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class PostgreSqlService {

    private WorkLogRepository worklogRepro;
    private JiraProjectRepository projectRepro;
    private JiraIssueRepository issueRepro;

    public PostgreSqlService(WorkLogRepository worklogRepro, JiraIssueRepository issueRepro
            , JiraProjectRepository projectRepro) {
        this.worklogRepro = worklogRepro;
        this.issueRepro = issueRepro;
        this.projectRepro = projectRepro;
    }

    public int loadWorkLog(Integer lastDays) {
        List<WorkLog> rw = this.worklogRepro.findByCreationDate(ZonedDateTime.now().minusDays(lastDays));
        rw.forEach(w -> loadIssue(w));
        return rw.size();
    }

    private void loadIssue(WorkLog workLog) {
        Optional<JiraIssue> issueOpt = issueRepro.findById(workLog.getIssueid());
        if (issueOpt.isPresent()) {
            JiraIssue issue = issueOpt.get();
            Optional<JiraProject> projectOpt = projectRepro.findById(Long.valueOf(issue.getProject()));
            if(projectOpt.isPresent()){
                log.info("user: "+workLog.getAuthor()+" date: "+ workLog.getStartdate() +" timeWorked:"+ workLog.getTimeworked()+" issue:"+issue.getSummary()+" project:"+projectOpt.get().getPname());
            }
        }

    }
}
