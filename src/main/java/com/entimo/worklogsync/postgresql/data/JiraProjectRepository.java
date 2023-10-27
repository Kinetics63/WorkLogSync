package com.entimo.worklogsync.postgresql.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JiraProjectRepository extends JpaRepository<JiraProject, Long> {
    @Query("SELECT project FROM JiraProject project WHERE UPPER(project.pname) = UPPER(:name)")
    List<JiraProject> findByProjectName(@Param("name") String name);
}
