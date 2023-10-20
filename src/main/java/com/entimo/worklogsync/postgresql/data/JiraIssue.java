package com.entimo.worklogsync.postgresql.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Entity
@Table(schema = "public", name = "jiraissue")
public class JiraIssue {

  @Id
  private Long id;
  private String pkey;
  private Integer issuenum;
  private Integer project;
  private String reporter;
  private String assignee;
  private String creator;
  private String issuetype;
  private String summary;
  private String description;
  private String environment;
  private String issuestatus;
  private ZonedDateTime updated;
  private ZonedDateTime created;
  private Long component;

  // temporary process information
  @Transient
  JiraProject jiraProject;

  @Transient
  JiraComponent jiraComponent;
}
