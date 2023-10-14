package com.entimo.worklogsync.postgresql.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Entity
@Table(schema = "public", name = "project")
public class Project {

  @Id
  private Long id;
  private String pname;
  private String description;
}
