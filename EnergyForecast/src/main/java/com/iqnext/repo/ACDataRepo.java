package com.iqnext.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iqnext.model.ACTimeSeries;



public interface ACDataRepo extends JpaRepository<ACTimeSeries,Long> {
	@Query(value = "SELECT * FROM iq_ac_time_series WHERE ac_id = ?1 AND ac_mode != 0;", nativeQuery = true)
	List<ACTimeSeries>getACState(String ac_id);

}
