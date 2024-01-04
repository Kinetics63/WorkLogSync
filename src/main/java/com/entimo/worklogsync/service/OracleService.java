package com.entimo.worklogsync.service;

import com.entimo.worklogsync.exception.WorkLogSyncException;
import com.entimo.worklogsync.oracle.data.IstStunden;
import com.entimo.worklogsync.oracle.data.IstStundenRepository;
import com.entimo.worklogsync.oracle.data.KstGruppe;
import com.entimo.worklogsync.oracle.data.KstGruppeRepository;
import com.entimo.worklogsync.oracle.data.PepProject;
import com.entimo.worklogsync.oracle.data.PepProjectRepository;

import com.entimo.worklogsync.utile.ProjectUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.*;

import com.entimo.worklogsync.utile.WorkLogEntry;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OracleService {

  private KstGruppeRepository kstGruppeRepo;
  private PepProjectRepository projectRepo;
  private IstStundenRepository istStundenRepo;

  public OracleService(KstGruppeRepository kstGruppeRepo, PepProjectRepository projectRepo,
      IstStundenRepository istStundenRepo) {
    this.kstGruppeRepo = kstGruppeRepo;
    this.projectRepo = projectRepo;
    this.istStundenRepo = istStundenRepo;
  }

  public PepProject loadProject(String projLang, String projKurz) {
    List<PepProject> proj = projectRepo.findByProjLangAndKurz(projLang, projKurz);
    if (proj.size() == 1) {
      return proj.get(0);
    }
    return null;
  }

  public List<PepProject> loadPepProjectsForUser(String persKurz) {
    List<PepProject> result = projectRepo.findProjectForUser(persKurz);
    result.forEach(this::addParent);
    return result;
  }

  private void addParent(PepProject project) {
    Optional<PepProject> projectOpt = projectRepo.findById(project.getParent());
    projectOpt.ifPresent(project::setParentProject);
  }

  public void processWorkLog(WorkLogEntry workLogEntry, List<String> logList) {
    List<KstGruppe> byPerskurz = kstGruppeRepo.findByPerskurz(workLogEntry.getAuthor().toUpperCase());
    if (byPerskurz.isEmpty()) {
      log.debug("User {} not found in PEP!", workLogEntry.getAuthor());
    } else {
      String pepProjecktLang = ProjectUtil.mapJiraToPep(workLogEntry.getJiraProjectName());
      String pepSubProjecktKurz = ProjectUtil.mapJiraToPepKurz(workLogEntry.getJiraProjectName());
      // Jira project not mapped to PEP
      if (!StringUtils.isEmpty(pepSubProjecktKurz)) {
        List<PepProject> pepProjList = projectRepo.findByProjLangAndKurz(pepProjecktLang, pepSubProjecktKurz);
        String subProjectLang = pepProjList.isEmpty() ? "???" : pepProjList.get(0).getLang();
        Long subProjectId = pepProjList.isEmpty() ? null : pepProjList.get(0).getId();

        //find pep Project for JiraProject
        List<PepProject> pepProjects = projectRepo.loadProjectForUser(workLogEntry.getAuthor(), pepSubProjecktKurz);
        // check if subproject assigned to user
        Optional<PepProject> pepSubProject = pepProjects.stream().filter(h -> Objects.equals(h.getKurz(), pepSubProjecktKurz)).findFirst();
        if (pepSubProject.isEmpty()) {
          logList.add("PEP project ("+subProjectLang+") not assigned to user " + workLogEntry.getAuthor());
        } else {
          Long pepUserKennNr = byPerskurz.get(0).getKennummer();
          List<IstStunden> pepIstStundenList = istStundenRepo.findByUserMonthYear(pepUserKennNr, workLogEntry.getMonth(), workLogEntry.getYear());
          Optional<IstStunden> pepIstStunden = pepIstStundenList.stream().filter(h -> Objects.equals(h.getPrjid(), subProjectId)).findFirst();
          IstStunden istStunden;
          if (pepIstStunden.isPresent()) {
            istStunden = pepIstStunden.get();
          } else {
            istStunden = new IstStunden();
            // get oracle sequence for table IstStunden
            Long sequence = istStundenRepo.getSequence();
            istStunden.setId(sequence);
            istStunden.setKennummer(pepUserKennNr);
            istStunden.setPrjid(subProjectId);
            istStunden.setMonth(workLogEntry.getMonth());
            istStunden.setYear(workLogEntry.getYear());
            istStunden.setFreigabe(0);
            istStunden.setLocked(0);
          }

          setHoursByReflection(workLogEntry.getAuthor(), workLogEntry.getDay(), istStunden, workLogEntry.getHours(),
                workLogEntry.getJiraProjectName() + " (" + subProjectLang + ")", logList);

        }
      }
    }
  }

  private void setHoursByReflection(String user, int day, IstStunden istStunden, Float timeWorked,
      String projectName, List<String> logList) {
    String logStr =
        user + " -> " + istStunden.getYear() + "/" + istStunden.getMonth() + "/" + day + "  hours: "
            + timeWorked + " project: " + projectName;
    try {
      Method getDayMethod = istStunden.getClass()
          .getDeclaredMethod("getDay" + day);
      Float hours = (Float) getDayMethod.invoke(istStunden);
      if (hours == null || hours == 0) {
        Method setDayMethod = istStunden.getClass()
            .getDeclaredMethod("setDay" + day, Float.class);
        setDayMethod.invoke(istStunden, timeWorked);
        sumHoursAndSave(istStunden);
        logList.add("update     : " + logStr);

      } else {
        logList.add("not updated (worklog already recorded): " + logStr);
      }
    } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
      throw new WorkLogSyncException("Reflection problem occurred", e);
    }
  }

  private void sumHoursAndSave(IstStunden istStunden) {
    istStunden.setSum(Float.valueOf("0"));
    addDayToSum(istStunden, istStunden.getDay1());
    addDayToSum(istStunden, istStunden.getDay2());
    addDayToSum(istStunden, istStunden.getDay3());
    addDayToSum(istStunden, istStunden.getDay4());
    addDayToSum(istStunden, istStunden.getDay5());
    addDayToSum(istStunden, istStunden.getDay6());
    addDayToSum(istStunden, istStunden.getDay7());
    addDayToSum(istStunden, istStunden.getDay8());
    addDayToSum(istStunden, istStunden.getDay9());
    addDayToSum(istStunden, istStunden.getDay10());
    addDayToSum(istStunden, istStunden.getDay11());
    addDayToSum(istStunden, istStunden.getDay12());
    addDayToSum(istStunden, istStunden.getDay13());
    addDayToSum(istStunden, istStunden.getDay14());
    addDayToSum(istStunden, istStunden.getDay15());
    addDayToSum(istStunden, istStunden.getDay16());
    addDayToSum(istStunden, istStunden.getDay17());
    addDayToSum(istStunden, istStunden.getDay18());
    addDayToSum(istStunden, istStunden.getDay18());
    addDayToSum(istStunden, istStunden.getDay19());
    addDayToSum(istStunden, istStunden.getDay20());
    addDayToSum(istStunden, istStunden.getDay21());
    addDayToSum(istStunden, istStunden.getDay22());
    addDayToSum(istStunden, istStunden.getDay23());
    addDayToSum(istStunden, istStunden.getDay24());
    addDayToSum(istStunden, istStunden.getDay25());
    addDayToSum(istStunden, istStunden.getDay26());
    addDayToSum(istStunden, istStunden.getDay27());
    addDayToSum(istStunden, istStunden.getDay28());
    addDayToSum(istStunden, istStunden.getDay29());
    addDayToSum(istStunden, istStunden.getDay30());
    addDayToSum(istStunden, istStunden.getDay31());
    istStundenRepo.save(istStunden);
  }

  private void addDayToSum(IstStunden istStunden, Float dayHours) {
    if (dayHours != null) {
      istStunden.setSum(istStunden.getSum() + dayHours);
    }
  }
}
