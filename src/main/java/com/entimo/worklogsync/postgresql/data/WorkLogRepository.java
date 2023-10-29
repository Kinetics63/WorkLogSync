package com.entimo.worklogsync.postgresql.data;

import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WorkLogRepository extends JpaRepository<WorkLog, Long> {

  List<WorkLog> findByAuthor(String author);

  @Query("SELECT wo FROM WorkLog wo WHERE wo.author = :author and wo.created>:creationDate order by wo.startdate")
  List<WorkLog> findByAuthorAndCreationDate(@Param("author") String author,
      @Param("creationDate") ZonedDateTime creationDate);

  @Query("SELECT wo FROM WorkLog wo WHERE wo.created>:creationDate order by wo.startdate")
  List<WorkLog> findByCreationDate(@Param("creationDate") ZonedDateTime creationDate);
}
