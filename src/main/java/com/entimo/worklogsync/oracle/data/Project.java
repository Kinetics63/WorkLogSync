package com.entimo.worklogsync.oracle.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(schema = "PEP", name = "PROJEKT")
public class Project {

  @Id
  @Column(name = "PRJ_ID")
  private Long id;
  @Column(name = "PRJ_NUMMER")
  private Long nummer;
  @Column(name = "PRJ_KURZ")
  private String kurz;
  @Column(name = "PRJ_LANG")
  private String lang;
}
