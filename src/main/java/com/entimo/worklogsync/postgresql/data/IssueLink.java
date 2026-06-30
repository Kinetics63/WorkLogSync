package com.entimo.worklogsync.postgresql.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Data;

@Data
@Entity
@Table(schema = "public", name = "issuelink")
public class IssueLink {

  @Id
  private Long id;
  private Long linktype;
  private Long source;
  private Long destination;
  private Long sequence;
}
