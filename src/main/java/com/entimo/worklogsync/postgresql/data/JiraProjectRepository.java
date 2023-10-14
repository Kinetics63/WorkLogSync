package com.entimo.worklogsync.postgresql.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JiraProjectRepository extends JpaRepository<JiraProject, Long> {
}
