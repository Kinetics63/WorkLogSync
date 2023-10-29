package com.entimo.worklogsync.oracle.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IstStundenRepository extends JpaRepository<IstStunden, Long> {

    @Query("SELECT wo FROM IstStunden wo WHERE wo.kennummer=:kennummer AND wo.month=:month AND wo.year=:year")
    List<IstStunden> findByUserMonthYear(@Param("kennummer") Long kennummer, @Param("month") Integer month, @Param("year") Integer year);

    @Query(value = "select PEP.ISTU_SEQ.nextval from dual", nativeQuery = true)
    Long getSequence();
}
