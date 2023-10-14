package com.entimo.worklogsync.oracle.data;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KstGruppeRepository extends JpaRepository<KstGruppe, Long> {

  List<KstGruppe> findByPerskurz(String perskurz);
}
