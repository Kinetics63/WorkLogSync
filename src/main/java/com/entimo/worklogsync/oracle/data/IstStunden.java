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
  private Float day1;
  @Column(name = "ISTU_TAG2")
  private Float day2;
  @Column(name = "ISTU_TAG3")
  private Float day3;
  @Column(name = "ISTU_TAG4")
  private Float day4;
  @Column(name = "ISTU_TAG5")
  private Float day5;
  @Column(name = "ISTU_TAG6")
  private Float day6;
  @Column(name = "ISTU_TAG7")
  private Float day7;
  @Column(name = "ISTU_TAG8")
  private Float day8;
  @Column(name = "ISTU_TAG9")
  private Float day9;
  @Column(name = "ISTU_TAG10")
  private Float day10;
  @Column(name = "ISTU_TAG11")
  private Float day11;
  @Column(name = "ISTU_TAG12")
  private Float day12;
  @Column(name = "ISTU_TAG13")
  private Float day13;
  @Column(name = "ISTU_TAG14")
  private Float day14;
  @Column(name = "ISTU_TAG15")
  private Float day15;
  @Column(name = "ISTU_TAG16")
  private Float day16;
  @Column(name = "ISTU_TAG17")
  private Float day17;
  @Column(name = "ISTU_TAG18")
  private Float day18;
  @Column(name = "ISTU_TAG19")
  private Float day19;
  @Column(name = "ISTU_TAG20")
  private Float day20;
  @Column(name = "ISTU_TAG21")
  private Float day21;
  @Column(name = "ISTU_TAG22")
  private Float day22;
  @Column(name = "ISTU_TAG23")
  private Float day23;
  @Column(name = "ISTU_TAG24")
  private Float day24;
  @Column(name = "ISTU_TAG25")
  private Float day25;
  @Column(name = "ISTU_TAG26")
  private Float day26;
  @Column(name = "ISTU_TAG27")
  private Float day27;
  @Column(name = "ISTU_TAG28")
  private Float day28;
  @Column(name = "ISTU_TAG29")
  private Float day29;
  @Column(name = "ISTU_TAG30")
  private Float day30;
  @Column(name = "ISTU_TAG31")
  private Float day31;
  @Column(name = "ISTU_SUM_MONAT")
  private Float sum;
  @Column(name = "ISTU_FREIGABE")
  private Integer freigabe;
  @Column(name = "ISTU_LOCKED")
  private Integer locked;
}
