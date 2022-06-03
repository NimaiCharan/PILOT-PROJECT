package com.iqnext.repo;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iqnext.model.ACTimeSeries;



public interface ACDataRepo extends JpaRepository<ACTimeSeries,Long> {
	
	//@Query(value = "SELECT * FROM iq_ac_time_series WHERE ac_id = ?1 AND ac_mode != 0 //AND MONTH(date)=MONTH(now()) and YEAR(date)=YEAR(now());", nativeQuery = true)
	@Query(value = "SELECT * FROM iq_ac_time_series WHERE ac_id = ?1 AND ac_mode != 0 ;", nativeQuery = true)
	List<ACTimeSeries>getACState(String ac_id);
	
	
	//@Query(value = "select date, count(*) from iq_ac_time_series where ac_id = ?1 AND MONTH(date)=MONTH(now()) and YEAR(date)=YEAR(now()) and ac_mode!=0 group by date;", nativeQuery = true)
	@Query(value = "select date, count(*) from iq_ac_time_series where ac_id = ?1 ;", nativeQuery = true)
	List<Object[]>getDayWiseResult(String ac_id);

}
