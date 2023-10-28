package com.entimo.worklogsync.utile;

import com.entimo.worklogsync.exception.WorkLogSyncException;
import com.entimo.worklogsync.postgresql.data.JiraProject;

import java.util.HashMap;
import java.util.Map;

public class ProjectUtil {

  public static final String JIRA_CLASSIC = "entimICE";
  public static final String JIRA_SHARED = "entimICE Shared";
  public static final String JIRA_NEXTGEN = "entimICE Next Generation";

  public static final String PEP_CLASSIC = "entimICE Basis";
  public static final String PEP_SHARED = "entimICE Shared";
  public static final String PEP_NEXTGEN = "entimICE Next Generation";


  private Map<String, Long> jiraProjects;

  public ProjectUtil() {
    jiraProjects = new HashMap<>();
  }

  public void addJiraProject(JiraProject pro) {
    jiraProjects.put(pro.getPname(), pro.getId());
  }

  public static String mapJiraToPep(String jiraProjectName){
    switch (jiraProjectName) {
      case JIRA_CLASSIC:
        return PEP_CLASSIC;
      case JIRA_SHARED:
        return PEP_SHARED;
      case JIRA_NEXTGEN:
        return PEP_NEXTGEN;
    }
    throw new WorkLogSyncException("Project "+jiraProjectName+" unknown!");
  }
}
