package com.entimo.worklogsync.postgresql.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.ZonedDateTime;
import lombok.Data;

@Data
@Entity
@Table(schema = "public", name = "label")
public class Label {

  @Id
  private Long id;
  private Long issue;
  private String label;
}
