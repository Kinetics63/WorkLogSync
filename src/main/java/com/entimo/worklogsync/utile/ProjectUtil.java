package com.entimo.worklogsync.utile;

import com.entimo.worklogsync.postgresql.data.JiraProject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class ProjectUtil {

    protected static final Logger log = LogManager.getLogger();

    // Jira projects currently found in WorkLogs
    public static final String JIRA_ROCHE = "1013_Roche";
    public static final String JIRA_Abbott = "Abbott";
    public static final String JIRA_DEVOPS = "DevOps";
    public static final String JIRA_ICELAB = "Ice Labs Work";
    public static final String JIRA_DESIGNCHAPTER = "NextGen Design Chapter";
    public static final String JIRA_WORKLOGS = "Work Logs";
    public static final String JIRA_CLASSIC = "entimICE";
    public static final String JIRA_SHARED = "entimICE Shared";
    public static final String JIRA_NEXTGEN = "entimICE Next Generation";
    public static final String JIRA_DOCUMENTATION = "entimICE Documentation";

    // PEP main projects (not assigned to user)
    public static final String PEP_CLASSIC = "entimICE Basis";
    public static final String PEP_SHARED = "entimICE Shared";
    public static final String PEP_NEXTGEN = "entimICE Next Generation";

    // PEP subprojects (assigned to user)
    public static final long PEP_NEXTGEN_UAD = 2341;
    private Map<String, Long> jiraProjects;

    public ProjectUtil() {
        jiraProjects = new HashMap<>();
    }

    public void addJiraProject(JiraProject pro) {
        jiraProjects.put(pro.getPname(), pro.getId());
    }

    public static String mapJiraToPep(String jiraProjectName) {
        switch (jiraProjectName) {
            case JIRA_CLASSIC:
                return PEP_CLASSIC;
            case JIRA_SHARED:
                return PEP_SHARED;
            case JIRA_NEXTGEN:
                return PEP_NEXTGEN;
            default:
                log.debug("Project {} unknown!", jiraProjectName);
                break;
        }
        return "";
    }

    public static Long mapPepToPepSub(String pepProjectName) {
        switch (pepProjectName) {
            case JIRA_CLASSIC:
                return 0L;
            case JIRA_SHARED:
                return 0L;
            case JIRA_NEXTGEN:
                return PEP_NEXTGEN_UAD;
            default:
                log.debug("Project {} unknown!", pepProjectName);
                break;
        }
        return 0L;
    }
}
