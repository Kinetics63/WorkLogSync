package com.entimo.worklogsync.utile;

import com.entimo.worklogsync.oracle.data.PepProject;
import com.entimo.worklogsync.postgresql.data.JiraProject;

import java.util.HashMap;
import java.util.Map;

public class ProjectUtil {

    protected static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ProjectUtil.class);

    // Jira projects currently found in WorkLogs
    public static final String JIRA_ROCHE = "1013_Roche";
    public static final String JIRA_Abbott = "Abbott";
    public static final String JIRA_DEVOPS = "DevOps";
    public static final String JIRA_ICELAB = "Ice Labs Work";
    public static final String JIRA_DESIGNCHAPTER = "NextGen Design Chapter";
    public static final String JIRA_WORKLOGS = "Work Logs";
    public static final String JIRA_CLASSIC = "entimICE";
    public static final String JIRA_SHARED = "entimICE Shared"; // deprecated
    public static final String JIRA_NEXTGEN = "entimICE Next Generation";
    public static final String JIRA_DOCUMENTATION = "entimICE Documentation";

    // PEP main projects (not assigned to user)
    public static final String PEP_DARE = "entimICE Basis";
    public static final String PEP_SHARED = "entimICE Shared";  // deprecated
    public static final String PEP_NEXTGEN = "entimICE Next Generation";
    public static final String PEP_DOCUMENTATION = "entimICE Dokumentation FastTrack";

    // PEP subprojects (assigned to user)
    public static final String PEP_DARE_DEV = "ICE_DEV";
    public static final String PEP_NEXTGEN_DEV = "ING_DEV";
    public static final String PEP_DOCUMENTATION_USERGUID = "UG";
    private Map<String, Long> jiraProjects;

    public ProjectUtil() {
        jiraProjects = new HashMap<>();
    }

    public void addJiraProject(JiraProject pro) {
        jiraProjects.put(pro.getPname(), pro.getId());
    }
    public void addPepProject(PepProject pro) {
        jiraProjects.put(pro.getKurz()+"/"+pro.getLang(), pro.getId());
    }

    public static String mapJiraToPepKurz(String jiraProjectName) {
        switch (jiraProjectName) {
            case JIRA_CLASSIC:
                return PEP_DARE_DEV;
            case JIRA_NEXTGEN:
                return PEP_NEXTGEN_DEV;
            case JIRA_DOCUMENTATION:
                return PEP_DOCUMENTATION_USERGUID;
            default:
                log.debug("Project {} not mapped to PEP!", jiraProjectName);
                break;
        }
        return "";
    }
    public static String mapJiraToPep(String jiraProjectName) {
        switch (jiraProjectName) {
            case JIRA_CLASSIC:
                return PEP_DARE;
            case JIRA_NEXTGEN:
                return PEP_NEXTGEN;
            case JIRA_DOCUMENTATION:
                return PEP_DOCUMENTATION;
            default:
                log.debug("Project {} not mapped to PEP!", jiraProjectName);
                break;
        }
        return "";
    }

}
