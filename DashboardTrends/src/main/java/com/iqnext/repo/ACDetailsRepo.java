package com.iqnext.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iqnext.model.ACDetails;

public interface ACDetailsRepo extends JpaRepository<ACDetails,Long>{
	
	List<ACDetails> findAll();

}
