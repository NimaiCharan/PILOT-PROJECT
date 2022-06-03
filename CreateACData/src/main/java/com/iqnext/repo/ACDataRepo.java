package com.iqnext.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iqnext.model.ACTimeSeries;



public interface ACDataRepo extends JpaRepository<ACTimeSeries,Long> {
	

}
