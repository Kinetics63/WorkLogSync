package com.entimo.worklogsync.service;

import com.entimo.worklogsync.exception.WorkLogSyncException;
import com.entimo.worklogsync.oracle.data.IstStunden;
import com.entimo.worklogsync.oracle.data.IstStundenRepository;
import com.entimo.worklogsync.oracle.data.KstGruppe;
import com.entimo.worklogsync.oracle.data.KstGruppeRepository;
import com.entimo.worklogsync.oracle.data.PepProject;
import com.entimo.worklogsync.oracle.data.PepProjectRepository;
import com.entimo.worklogsync.postgresql.data.WorkLog;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.*;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class OracleService {

  private final DecimalFormat hourFormat = new DecimalFormat("0.00");
  private KstGruppeRepository kstGruppeRepo;
  private PepProjectRepository projectRepo;
  private IstStundenRepository istStundenRepo;

  public OracleService(KstGruppeRepository kstGruppeRepo, PepProjectRepository projectRepo,
      IstStundenRepository istStundenRepo) {
    this.kstGruppeRepo = kstGruppeRepo;
    this.projectRepo = projectRepo;
    this.istStundenRepo = istStundenRepo;
  }

  public List<PepProject> loadPepProjectsForUser(String persKurz) {
    List<PepProject> result = projectRepo.findProjectForUser(persKurz.toUpperCase());
    result.forEach(this::addParent);
    return result;
  }

  private void addParent(PepProject project) {
    Optional<PepProject> projectOpt = projectRepo.findById(project.getParent());
    projectOpt.ifPresent(project::setParentProject);
  }

  public void processWorkLogs(List<WorkLog> workLogs) {
    for (WorkLog workLog : workLogs) {
      List<KstGruppe> byPerskurz = kstGruppeRepo.findByPerskurz(workLog.getAuthor().toUpperCase());
      if (byPerskurz.isEmpty()) {
        log.error("User {} not found in PEP!", workLog.getAuthor());
      } else {
        Long kennummer = byPerskurz.get(0).getKennummer();
        int month = workLog.getCreated().getMonth().ordinal();
        int year = workLog.getCreated().getYear();
        List<IstStunden> pepIstStundenList =
            istStundenRepo.findByUserMonthYear(kennummer,month + 1, year);

        // @ToDo if list is null create new object - depends on project

        // @ToDo collect worklogs per user/day/project

        // @ToDo map project from Jira to PEP

        //
        SortedMap<String, String> logMap = new TreeMap<>();

        // just for the demo
        if (workLog.getAuthor().equalsIgnoreCase("rw")) {
          setHoursForUAD(workLog, pepIstStundenList, logMap);
        }

        logMap.forEach((key, value) -> log.info((key+" : "+value)));
      }
    }
  }

  private void setHoursForUAD(WorkLog workLog, List<IstStunden> pepHours, Map<String, String> logMap) {
    Optional<IstStunden> first = pepHours.stream().filter(h -> h.getPrjid() == 2341).findFirst();
    if (first.isPresent()) {
      IstStunden istStunden = first.get();
      Long timeworked = workLog.getTimeworked();
      int day = workLog.getStartdate().getDayOfMonth();
      double v = timeworked / 60.0 / 60.0;
      setHoursByReflection(workLog.getAuthor(), day, istStunden, (float) v, logMap);
    }

  }

  private void setHoursByReflection(String user, int day, IstStunden istStunden, Float timeWorked, Map<String,String> logMap) {

    String logStr = user+" -> Month/Day: "+istStunden.getMonth()+"/"+day+ "  hours: "+ timeWorked;
    try {
      Method getDayMethod = istStunden.getClass()
          .getDeclaredMethod("getDay" + day);
      Float hours = (Float) getDayMethod.invoke(istStunden);
      if (hours == null || hours == 0) {
        Method setDayMethod = istStunden.getClass()
            .getDeclaredMethod("setDay" + day, Float.class);
        setDayMethod.invoke(istStunden, timeWorked);
        sumHoursAndSave(istStunden);

        logMap.put("update", logStr);

      }else{
        logMap.put("not updated", logStr);
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
