package com.entimo.worklogsync.service;

import com.entimo.worklogsync.postgresql.data.*;

import java.time.ZonedDateTime;
import java.util.*;

import com.entimo.worklogsync.utile.WorkLogEntry;
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

    public JiraProject loadProject(String name) {
        List<JiraProject> proj = projectRepro.findByProjectName(name);
        if (proj.size() == 1) {
            return proj.get(0);
        }
        return null;
    }

    public Map<String, WorkLogEntry> loadWorkLog(Integer lastDays) {
        List<WorkLog> workLogs = this.worklogRepro.findByCreationDate(ZonedDateTime.now().minusDays(lastDays));
        workLogs.forEach(this::loadIssue);
        Map<String, WorkLogEntry> logMap = new HashMap<>();
        workLogs.forEach(wl -> cumulateHours(wl, logMap));

        log.info("Found {} work logs in JIRA which are cumulated to {} project related user work logs for the last {} days.", workLogs.size(), logMap.size(), lastDays);

        // log found jira projects
        if (log.isDebugEnabled()) {
            Set<String> set = new TreeSet<>();
            workLogs.forEach(w -> set.add(w.getJiraProjectName()));
            log.debug("found jira projects:");
            set.forEach(s -> log.info("   " + s));
        }
        return logMap;
    }

    private void cumulateHours(WorkLog workLog, Map<String, WorkLogEntry> logMap) {
        if (logMap.containsKey(workLog.uniqueString())) {
            WorkLogEntry workLogEntry = logMap.get(workLog.uniqueString());
            double v = workLog.getTimeworked() / 60.0 / 60.0;
            workLogEntry.setHours(workLogEntry.getHours() + (float) v);
        } else {
            WorkLogEntry workLogEntry = new WorkLogEntry(workLog);
            logMap.put(workLog.uniqueString(), workLogEntry);
        }
    }

    private void loadIssue(WorkLog workLog) {
        Optional<JiraIssue> issueOpt = issueRepro.findById(workLog.getIssueid());
        if (issueOpt.isPresent()) {
            JiraIssue issue = issueOpt.get();
            workLog.setIssue(issue);
            Optional<JiraProject> projectOpt = projectRepro.findById(Long.valueOf(issue.getProject()));
            if (projectOpt.isPresent()) {
                issue.setJiraProject(projectOpt.get());
            }
            if (issue.getComponent() != null) {
                Optional<JiraComponent> componentOpt = componentRepro.findById(issue.getComponent());
                if (componentOpt.isPresent()) {
                    issue.setJiraComponent(componentOpt.get());
                }
            }
            log.debug("USER: " + workLog.getAuthor() + " DATE: " + workLog.getStartdate() + " TIME: " + workLog.getTimeworked() + " ISSUE: " + issue.getSummary());
            log.debug("   COMPONENT:" + (issue.getJiraComponent() != null ? issue.getJiraComponent().getName() : " ??? ") + "   PROJECT: " + (issue.getJiraProject() != null ? issue.getJiraProject().getPname() : " ??? "));
        }
    }
}
