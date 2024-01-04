package com.entimo.worklogsync.oracle.data;

import java.util.List;

import com.entimo.worklogsync.postgresql.data.JiraProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PepProjectRepository extends JpaRepository<PepProject, Long> {

  @Query("SELECT project FROM PepProject project WHERE UPPER(project.lang) = UPPER(:name)")
  List<PepProject> findByProjectName(@Param("name") String name);

  @Query(
      value = "select p.* "
          + "from PEP.PROJEKT c, PEP.PROJEKT p "
          + "where UPPER(c.PRJ_LANG)=UPPER(:projLang) "
          + "and UPPER(p.PRJ_KURZ) = UPPER(:projKurz) "
          + "and p.PRJ_PRJ_PARENT_ID = c.PRJ_ID ",
      nativeQuery = true)
  List<PepProject> findByProjLangAndKurz(@Param("projLang") String projLang, @Param("projKurz") String projKurz);

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
          + "and UPPER(c.PRJ_KURZ) = UPPER(:projKurz) "
          + "and c.PRJ_PRJ_PARENT_ID = p.PRJ_ID ",
      nativeQuery = true)
  List<PepProject>  loadProjectForUser(@Param("perskurz") String perskurz, @Param("projKurz") String projKurz);
}
