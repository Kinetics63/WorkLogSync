package com.entimo.worklogsync.utile;

import com.entimo.worklogsync.postgresql.data.JiraProject;

import java.util.HashMap;
import java.util.Map;

public class ProjectUtil {

   public static final String CLASSIC ="entimICE";
    public static final String SHARED ="entimICE Shared";
    public static final String NEXTGEN ="entimICE Next Generation";

    private Map<String, Long> jiraProjects;
    public ProjectUtil() {
        jiraProjects = new HashMap<>();
    }

    public void addJiraProject(JiraProject pro){
        jiraProjects.put(pro.getPname(), pro.getId());
    }
}
