package com.entimo.worklogsync.oracle.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import lombok.Data;

@Data
@Entity
@Table(schema = "PEP", name = "KST_GRUPPE")
public class KstGruppe {

  @Id
  private Long kennummer;
  private Long persnr;
  private String perskurz;
  private String name;
}
