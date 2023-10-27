package com.entimo.worklogsync.utile;

import com.entimo.worklogsync.postgresql.data.WorkLog;
import lombok.Data;

@Data
public class WorkLogEntry {

    private String author;

    private Long jiraProjectId;
    private Long pepProjectId;

    private Float hours;

    private Integer year;
    private Integer month;
    private Integer day;

    public WorkLogEntry(WorkLog workLog) {
        author = workLog.getAuthor();
        jiraProjectId = workLog.getJiraProjectId();
        hours = workLog.getTimeworked() / (float) 60.0 / (float) 60.0;
        year = workLog.getStartdate().getYear();
        month = workLog.getStartdate().getMonthValue();
        day = workLog.getStartdate().getDayOfMonth();
    }
}
