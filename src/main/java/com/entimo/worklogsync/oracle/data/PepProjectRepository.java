package com.entimo.worklogsync.oracle.data;

import com.entimo.worklogsync.postgresql.data.WorkLog;
import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PepProjectRepository extends JpaRepository<PepProject, Long> {

  @Query(
      value = "select PEP.PROJEKT.* "
          + "from PEP.KST_GRUPPE, PEP.PRJ_MA, PEP.PROJEKT "
          + "where PEP.PROJEKT.PRJ_ID = PEP.PRJ_MA.PRJMA_PRJ_ID "
          + "and PEP.PRJ_MA.PRJMA_KENNUMMER = PEP.KST_GRUPPE.KENNUMMER "
          + "and PEP.KST_GRUPPE.PERSKURZ=:perskurz order by PEP.PROJEKT.PRJ_ID desc",
      nativeQuery = true)
  List<PepProject> findProjectForUser(@Param("perskurz") String perskurz);
}
