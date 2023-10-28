package com.entimo.worklogsync.oracle.data;

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
          + "and UPPER(PEP.KST_GRUPPE.PERSKURZ)=UPPER(:perskurz) "
          + "order by PEP.PROJEKT.PRJ_ID desc",
      nativeQuery = true)
  List<PepProject> findProjectForUser(@Param("perskurz") String perskurz);

  @Query(
      value = "select c.* "
          + "from PEP.KST_GRUPPE, PEP.PRJ_MA, PEP.PROJEKT c, PEP.PROJEKT p "
          + "where c.PRJ_ID = PEP.PRJ_MA.PRJMA_PRJ_ID "
          + "and PEP.PRJ_MA.PRJMA_KENNUMMER = PEP.KST_GRUPPE.KENNUMMER "
          + "and UPPER(PEP.KST_GRUPPE.PERSKURZ)=UPPER(:perskurz) "
          + "and UPPER(p.PRJ_LANG) = UPPER(:projLang) "
          + "and c.PRJ_PRJ_PARENT_ID = p.PRJ_ID ",
      nativeQuery = true)
  List<PepProject>  loadProjectForUser(@Param("perskurz") String perskurz, @Param("projLang") String projLang);
}
