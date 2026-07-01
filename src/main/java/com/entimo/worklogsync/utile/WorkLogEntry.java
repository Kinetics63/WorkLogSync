package com.entimo.worklogsync.utile;

import com.entimo.worklogsync.postgresql.data.Label;
import com.entimo.worklogsync.postgresql.data.WorkLog;
import java.util.List;
import lombok.Data;

@Data
public class WorkLogEntry {

    private String author;

    private Long jiraProjectId;
    private String jiraProjectName;
    private String jiraIssueKey;
    private List<Label> labelList;

    private Float hours;

    private Integer year;
    private Integer month;
    private Integer day;

    public WorkLogEntry(WorkLog workLog) {
        author = workLog.getAuthor();
        jiraProjectId = workLog.getJiraProjectId();
        jiraProjectName = workLog.getIssue().getJiraProject().getPname();
        hours = workLog.getTimeworked() / (float) 60.0 / (float) 60.0;
        year = workLog.getStartdate().getYear();
        month = workLog.getStartdate().getMonthValue();
        day = workLog.getStartdate().getDayOfMonth();
        labelList = workLog.getIssue().getLabel();
        jiraIssueKey = workLog.getIssue().getPkey()+"-"+workLog.getIssue().getIssuenum();
    }
}
