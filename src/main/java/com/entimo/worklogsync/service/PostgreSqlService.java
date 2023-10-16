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

    public List<WorkLog> loadWorkLog(Integer lastDays) {
        List<WorkLog> workLogs = this.worklogRepro.findByCreationDate(ZonedDateTime.now().minusDays(lastDays));
        workLogs.forEach(w -> loadIssue(w));
        return workLogs;
    }

    private void loadIssue(WorkLog workLog) {
        Optional<JiraIssue> issueOpt = issueRepro.findById(workLog.getIssueid());
        if (issueOpt.isPresent()) {
            JiraIssue issue = issueOpt.get();
            workLog.setIssue(issue);
            Optional<JiraProject> projectOpt = projectRepro.findById(Long.valueOf(issue.getProject()));
            if(projectOpt.isPresent()){
                issue.setJiraProject(projectOpt.get());
                log.info("user: "+workLog.getAuthor()+" date: "+ workLog.getStartdate() +" timeWorked:"+ workLog.getTimeworked()+" issue:"+issue.getSummary()+" project:"+projectOpt.get().getPname());
            }
        }
    }
}
