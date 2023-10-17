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
    private JiraComponentRepository componentRepro;

    public PostgreSqlService(WorkLogRepository worklogRepro, JiraIssueRepository issueRepro
        , JiraProjectRepository projectRepro, JiraComponentRepository componentRepro) {
        this.worklogRepro = worklogRepro;
        this.issueRepro = issueRepro;
        this.projectRepro = projectRepro;
        this.componentRepro = componentRepro;
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
            }
            if(issue.getComponent()!=null) {
                Optional<JiraComponent> componentOpt = componentRepro.findById(Long.valueOf(issue.getComponent()));
                if (componentOpt.isPresent()) {
                    issue.setJiraComponent(componentOpt.get());
                }
            }
            log.info("USER: "+workLog.getAuthor()+" DATE: "+ workLog.getStartdate() +" TIME: "+ workLog.getTimeworked()+" ISSUE: "+issue.getSummary());
            log.info("   COMPONENT:"+(issue.getJiraComponent()!=null?issue.getJiraComponent().getName():" ??? ")+"   PROJECT: "+(issue.getJiraProject()!=null?issue.getJiraProject().getPname():" ??? "));
        }
    }
}
