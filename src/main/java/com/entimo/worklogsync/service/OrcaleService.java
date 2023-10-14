package com.entimo.worklogsync.service;

import com.entimo.worklogsync.oracle.data.KstGruppeRepository;
import com.entimo.worklogsync.oracle.data.PepProjectRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class OrcaleService {

  private KstGruppeRepository kstGruppeRepo;
  private PepProjectRepository projectRepo;

  public OrcaleService(KstGruppeRepository kstGruppeRepo, PepProjectRepository projectRepo) {
    this.kstGruppeRepo = kstGruppeRepo;
    this.projectRepo = projectRepo;

 //   List<KstGruppe> rw = this.kstGruppeRepo.findByPerskurz("RW");
 //   rw.forEach(w->log.info(w.toString()));

//    List<Project> proj = this.projectRepo.findAll(Sort.by(Direction.DESC, "id"));
 //   proj.forEach(w->log.info(w.toString()));
  }
}
