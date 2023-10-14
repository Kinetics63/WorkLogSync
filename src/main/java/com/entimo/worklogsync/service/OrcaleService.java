package com.entimo.worklogsync.service;

import com.entimo.worklogsync.oracle.data.KstGruppe;
import com.entimo.worklogsync.oracle.data.KstGruppeRepository;
import com.entimo.worklogsync.oracle.data.Project;
import com.entimo.worklogsync.oracle.data.ProjectRepository;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.config.ProjectingArgumentResolverRegistrar;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class OrcaleService {

  private KstGruppeRepository kstGruppeRepo;
  private ProjectRepository projectRepo;

  public OrcaleService(KstGruppeRepository kstGruppeRepo, ProjectRepository projectRepo) {
    this.kstGruppeRepo = kstGruppeRepo;
    this.projectRepo = projectRepo;

 //   List<KstGruppe> rw = this.kstGruppeRepo.findByPerskurz("RW");
 //   rw.forEach(w->log.info(w.toString()));

//    List<Project> proj = this.projectRepo.findAll(Sort.by(Direction.DESC, "id"));
 //   proj.forEach(w->log.info(w.toString()));
  }
}
