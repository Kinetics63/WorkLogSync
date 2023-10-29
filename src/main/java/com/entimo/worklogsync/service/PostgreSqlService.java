package com.entimo.worklogsync.service;

import com.entimo.worklogsync.postgresql.data.*;

import java.time.ZonedDateTime;
import java.util.*;

import com.entimo.worklogsync.utile.WorkLogEntry;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class PostgreSqlService {

    private WorkLogRepository worklogRepo;
    private JiraProjectRepository projectRepo;
    private JiraIssueRepository issueRepo;
    private JiraComponentRepository componentRepo;

    public PostgreSqlService(WorkLogRepository worklogRepro, JiraIssueRepository issueRepro
            , JiraProjectRepository projectRepro, JiraComponentRepository componentRepro) {
        this.worklogRepo = worklogRepro;
        this.issueRepo = issueRepro;
        this.projectRepo = projectRepro;
        this.componentRepo = componentRepro;
    }

    public JiraProject loadProject(String name) {
        List<JiraProject> proj = projectRepo.findByProjectName(name);
        if (proj.size() == 1) {
            return proj.get(0);
        }
        return null;
    }

    public Map<String, WorkLogEntry> loadWorkLog(Integer lastDays, String user) {
        ZonedDateTime dateTime = ZonedDateTime.now().minusDays(lastDays);
        List<WorkLog> workLogs;
        if (StringUtils.isEmpty(user)) {
            workLogs = this.worklogRepo.findByCreationDate(dateTime);
        } else {
            workLogs = this.worklogRepo.findByAuthorAndCreationDate(user, dateTime);
        }
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
        Optional<JiraIssue> issueOpt = issueRepo.findById(workLog.getIssueid());
        if (issueOpt.isPresent()) {
            JiraIssue issue = issueOpt.get();
            workLog.setIssue(issue);
            Optional<JiraProject> projectOpt = projectRepo.findById(Long.valueOf(issue.getProject()));
            if (projectOpt.isPresent()) {
                issue.setJiraProject(projectOpt.get());
            }
            if (issue.getComponent() != null) {
                Optional<JiraComponent> componentOpt = componentRepo.findById(issue.getComponent());
                if (componentOpt.isPresent()) {
                    issue.setJiraComponent(componentOpt.get());
                }
            }
            log.debug("USER: " + workLog.getAuthor() + " DATE: " + workLog.getStartdate() + " TIME: " + workLog.getTimeworked() + " ISSUE: " + issue.getSummary());
            log.debug("   COMPONENT:" + (issue.getJiraComponent() != null ? issue.getJiraComponent().getName() : " ??? ") + "   PROJECT: " + (issue.getJiraProject() != null ? issue.getJiraProject().getPname() : " ??? "));
        }
    }
}
