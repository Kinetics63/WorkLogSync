package com.entimo.worklogsync.oracle.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(schema = "PEP", name = "ISTSTUNDEN")
public class IstStunden {

  @Id
  @Column(name = "ISTU_ID")
  private Long id;
  @Column(name = "ISTU_PRJ_ID")
  private Long prjid;
  @Column(name = "ISTU_KENNUMMER")
  private Long kennummer;
  @Column(name = "ISTU_MONAT")
  private Integer month;
  @Column(name = "ISTU_JAHR")
  private Integer year;
  @Column(name = "ISTU_TAG1")
  private Integer day1;
  @Column(name = "ISTU_TAG2")
  private Integer day2;
  @Column(name = "ISTU_TAG3")
  private Integer day3;
  @Column(name = "ISTU_TAG4")
  private Integer day4;
  @Column(name = "ISTU_TAG5")
  private Integer day5;
  @Column(name = "ISTU_TAG6")
  private Integer day6;
  @Column(name = "ISTU_TAG7")
  private Integer day7;
  @Column(name = "ISTU_TAG8")
  private Integer day8;
  @Column(name = "ISTU_TAG9")
  private Integer day9;
  @Column(name = "ISTU_TAG10")
  private Integer day10;
  @Column(name = "ISTU_TAG11")
  private Integer day11;
  @Column(name = "ISTU_TAG12")
  private Integer day12;
  @Column(name = "ISTU_TAG13")
  private Integer day13;
  @Column(name = "ISTU_TAG14")
  private Integer day14;
  @Column(name = "ISTU_TAG15")
  private Integer day15;
  @Column(name = "ISTU_TAG16")
  private Integer day16;
  @Column(name = "ISTU_TAG17")
  private Integer day17;
  @Column(name = "ISTU_TAG18")
  private Integer day18;
  @Column(name = "ISTU_TAG19")
  private Integer day19;
  @Column(name = "ISTU_TAG20")
  private Integer day20;
  @Column(name = "ISTU_TAG21")
  private Integer day21;
  @Column(name = "ISTU_TAG22")
  private Integer day22;
  @Column(name = "ISTU_TAG23")
  private Integer day23;
  @Column(name = "ISTU_TAG24")
  private Integer day24;
  @Column(name = "ISTU_TAG25")
  private Integer day25;
  @Column(name = "ISTU_TAG26")
  private Integer day26;
  @Column(name = "ISTU_TAG27")
  private Integer day27;
  @Column(name = "ISTU_TAG28")
  private Integer day28;
  @Column(name = "ISTU_TAG29")
  private Integer day29;
  @Column(name = "ISTU_TAG30")
  private Integer day30;
  @Column(name = "ISTU_TAG31")
  private Integer day31;
  @Column(name = "ISTU_SUM_MONAT")
  private Integer sum;
  @Column(name = "ISTU_FREIGABE")
  private Integer freigabe;
  @Column(name = "ISTU_LOCKED")
  private Integer locked;
}
