package com.entimo.worklogsync.postgresql.data;

import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JiraIssueRepository extends JpaRepository<JiraIssue, Long> {
}
