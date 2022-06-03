package com.iqnext.repo;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iqnext.model.WeatherData;

@Repository
public interface WeatherDataRepo extends JpaRepository<WeatherData,Long> {
	
	List<WeatherData> findAll();
	
	
	
	
	String queryBetweenDate = "select * from iq_weather_time_series"
			+ " where date between DATE_SUB(CURDATE(),INTERVAL ?1 DAY) "
			+ " and DATE_SUB(CURDATE(),INTERVAL ?2 DAY)";
	@Query(value = queryBetweenDate, nativeQuery = true)
	List<WeatherData> dayWiseRecords(int fromDay, int toDay);
	
	
}
