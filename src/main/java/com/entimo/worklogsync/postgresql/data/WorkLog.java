package com.entimo.worklogsync.postgresql.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.ZonedDateTime;
import lombok.Data;

@Data
@Entity
@Table(schema = "public", name = "worklog")
public class WorkLog {

  @Id
  private Long id;
  private Long issueid;
  private String author;
  private String grouplevel;
  private Long rolelevel;
  private String worklogbody;
  private ZonedDateTime created;
  private String updateauthor ;
  private ZonedDateTime updated ;
  private ZonedDateTime startdate ;
  private Long timeworked;

  // temporary process information
  @Transient
  JiraIssue issue;

}
