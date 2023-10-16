package com.entimo.worklogsync.service;

import com.entimo.worklogsync.oracle.data.IstStunden;
import com.entimo.worklogsync.oracle.data.IstStundenRepository;
import com.entimo.worklogsync.oracle.data.KstGruppe;
import com.entimo.worklogsync.oracle.data.KstGruppeRepository;
import com.entimo.worklogsync.oracle.data.PepProject;
import com.entimo.worklogsync.oracle.data.PepProjectRepository;
import com.entimo.worklogsync.postgresql.data.WorkLog;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class OracleService {

  private KstGruppeRepository kstGruppeRepo;
  private PepProjectRepository projectRepo;
  private IstStundenRepository istStundenRepo;

  public OracleService(KstGruppeRepository kstGruppeRepo, PepProjectRepository projectRepo,
      IstStundenRepository istStundenRepo) {
    this.kstGruppeRepo = kstGruppeRepo;
    this.projectRepo = projectRepo;
    this.istStundenRepo = istStundenRepo;

    //   List<KstGruppe> rw = this.kstGruppeRepo.findByPerskurz("RW");
    //   rw.forEach(w->log.info(w.toString()));

//    List<JiraProject> proj = this.projectRepo.findAll(Sort.by(Direction.DESC, "id"));
    //   proj.forEach(w->log.info(w.toString()));
  }

  public List<PepProject> loadPepProjectsForUser(String persKurz) {
    List<PepProject> result = projectRepo.findProjectForUser(persKurz.toUpperCase());
    return result;
  }

  public void loadMonthForUser(List<WorkLog> workLogs) {
    for (WorkLog workLog : workLogs) {
      List<KstGruppe> byPerskurz = kstGruppeRepo.findByPerskurz(workLog.getAuthor().toUpperCase());
      Long kennummer = byPerskurz.get(0).getKennummer();
      int month = workLog.getCreated().getMonth().ordinal();
      int year = workLog.getCreated().getYear();
      List<IstStunden> byKennummerAndMonth = istStundenRepo.findByKennummerAndMonth(kennummer,
          month + 1, year);
      log.info(byKennummerAndMonth.toString());
      setHorsForUAD(workLog, byKennummerAndMonth);

    }
  }

  private void setHorsForUAD(WorkLog workLog, List<IstStunden> hours) {
    Optional<IstStunden> first = hours.stream().filter(h -> h.getPrjid()==2341).findFirst();
    if(first.isPresent()){
      IstStunden istStunden = first.get();
      Long timeworked = workLog.getTimeworked();
      int day = workLog.getStartdate().getDayOfMonth();
      setHoursByRelection(day, istStunden, timeworked/60/60);
    }

  }

  private void setHoursByRelection(int day, IstStunden istStunden, Long timeWorked) {

    Field field = null;
    try {
      Method[] declaredMethods = istStunden.getClass().getDeclaredMethods();
      Method declaredMethod = istStunden.getClass().getDeclaredMethod("setDay" + day, Integer.class);
      declaredMethod.setAccessible(true);
      declaredMethod.invoke(istStunden,timeWorked.intValue() );
      // save time
      istStundenRepo.save(istStunden);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }
}
