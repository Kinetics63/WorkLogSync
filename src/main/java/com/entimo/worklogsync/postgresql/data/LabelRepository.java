package com.entimo.worklogsync.postgresql.data;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LabelRepository extends JpaRepository<Label, Long> {

  @Query("SELECT la FROM JiraIssue is, Label la, IssueLink il "
      + "WHERE il.destination=:id "
      + "AND il.source = is.id "
      + "AND la.issue = is.id")
  List<Label> findByParentIssue(@Param("id") Long id);

}
