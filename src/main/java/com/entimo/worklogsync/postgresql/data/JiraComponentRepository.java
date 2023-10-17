package com.entimo.worklogsync.postgresql.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JiraComponentRepository extends JpaRepository<JiraComponent, Long> {
}
