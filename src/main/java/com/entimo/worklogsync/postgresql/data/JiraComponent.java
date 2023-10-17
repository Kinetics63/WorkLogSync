package com.entimo.worklogsync.postgresql.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(schema = "public", name = "component")
public class JiraComponent {

  @Id
  @Column(name = "id")
  private Long id;
  @Column(name = "project")
  private Long project;
  @Column(name = "cname")
  private String name;
}
