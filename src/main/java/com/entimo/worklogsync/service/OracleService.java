package com.entimo.worklogsync.service;

import com.entimo.worklogsync.exception.WorkLogSyncException;
import com.entimo.worklogsync.oracle.data.IstStunden;
import com.entimo.worklogsync.oracle.data.IstStundenRepository;
import com.entimo.worklogsync.oracle.data.KstGruppe;
import com.entimo.worklogsync.oracle.data.KstGruppeRepository;
import com.entimo.worklogsync.oracle.data.PepProject;
import com.entimo.worklogsync.oracle.data.PepProjectRepository;
import com.entimo.worklogsync.postgresql.data.JiraProject;
import com.entimo.worklogsync.postgresql.data.WorkLog;
import com.entimo.worklogsync.utile.ProjectUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.*;

import com.entimo.worklogsync.utile.WorkLogEntry;
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
    }

    public PepProject loadProject(String name) {
        List<PepProject> proj = projectRepo.findByProjectName(name);
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
            String pepMainProject = ProjectUtil.mapJiraToPep(workLogEntry.getJiraProjectName());
            Long pepSubProjeckt = ProjectUtil.mapPepToPepSub(pepMainProject);
            Optional<PepProject> byId = projectRepo.findById(pepSubProjeckt);
            String subProjectName =  byId.isEmpty()?"???":byId.get().getLang();

            //find pep Project for JiraProject
            List<PepProject> pepProjects = projectRepo.loadProjectForUser(workLogEntry.getAuthor(), pepMainProject);
            // check if subproject assigned to user
            Optional<PepProject> pepSubProject = pepProjects.stream().filter(h -> Objects.equals(h.getId(), pepSubProjeckt)).findFirst();
            if (pepSubProject.isEmpty()) {
                logList.add("pep project not assigned to user " + workLogEntry.getAuthor() + " project: " + pepMainProject + " (" + subProjectName + ")");
            } else {
                Long pepUserKennNr = byPerskurz.get(0).getKennummer();
                List<IstStunden> pepIstStundenList =
                        istStundenRepo.findByUserMonthYear(pepUserKennNr, workLogEntry.getMonth(), workLogEntry.getYear());
                Optional<IstStunden> pepIstStunden = pepIstStundenList.stream().filter(h -> Objects.equals(h.getPrjid(), pepSubProjeckt)).findFirst();
                IstStunden istStunden;
                if (pepIstStunden.isPresent()) {
                    istStunden = pepIstStunden.get();
                } else {
                    istStunden = new IstStunden();
                    // get oracle sequence for table IstStunden
                    Long sequence = istStundenRepo.getSequence();
                    istStunden.setId(sequence);
                    istStunden.setKennummer(pepUserKennNr);
                    istStunden.setPrjid(pepSubProjeckt);
                    istStunden.setMonth(workLogEntry.getMonth());
                    istStunden.setYear(workLogEntry.getYear());
                    istStunden.setFreigabe(0);
                    istStunden.setLocked(0);
                }
                // filter RW just for the demo
                if (workLogEntry.getAuthor().equalsIgnoreCase("rw")) {
                    setHoursByReflection(workLogEntry.getAuthor(), workLogEntry.getDay(), istStunden, workLogEntry.getHours(), workLogEntry.getJiraProjectName()+ " (" + subProjectName + ")", logList);
                }
            }
        }
    }

    private void setHoursByReflection(String user, int day, IstStunden istStunden, Float timeWorked, String projectName, List<String> logList) {
        String logStr = user + " -> " + istStunden.getYear()+"/" +istStunden.getMonth() + "/" + day + "  hours: " + timeWorked + " project: " + projectName;
        try {
            Method getDayMethod = istStunden.getClass()
                    .getDeclaredMethod("getDay" + day);
            Float hours = (Float) getDayMethod.invoke(istStunden);
            if (hours == null || hours == 0) {
                Method setDayMethod = istStunden.getClass()
                        .getDeclaredMethod("setDay" + day, Float.class);
                setDayMethod.invoke(istStunden, timeWorked);
                sumHoursAndSave(istStunden);
                logList.add("update     : "+logStr);

            } else {
                logList.add("not updated: "+ logStr);
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
