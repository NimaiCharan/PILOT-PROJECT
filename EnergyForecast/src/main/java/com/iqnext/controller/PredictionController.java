package com.iqnext.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.iqnext.model.ACDetails;
import com.iqnext.model.ACTimeSeries;
import com.iqnext.repo.ACDataRepo;
import com.iqnext.repo.ACDetailsRepo;
import com.iqnext.utils.EnergyCalcUtils;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:4200")
public class PredictionController {
	
	
	@Autowired 
	WebClient.Builder webClientBuilder;
	
	@Autowired
	ACDetailsRepo acDetailsRepo;
	
	@Autowired
	ACDataRepo acDataRepo;
	
	@GetMapping("/")
	public String test() {
		
		String test = webClientBuilder.build()
		.get()
		.uri("https://jsonplaceholder.typicode.com/todos/1")
		.accept(MediaType.APPLICATION_JSON)
		.retrieve()
		.bodyToMono(String.class)
		.block();
		System.out.println(test);
		return "Nimai";
		
	}
	
	@GetMapping("/getTempPrediction")
	public Object[] getTempPrediction() {
		
		Mono<Object[]> response= webClientBuilder.build()
		.get()
		.uri("http://127.0.0.1:8000/api/v1/get_prediction")
		.accept(MediaType.APPLICATION_JSON)
		.retrieve()
		.bodyToMono(Object[].class)
		.log();
		Object[] objects = response.block();
		System.out.println(objects[0].toString());
			
		return objects;
		
	}
	@GetMapping("/get_ac_usage")
	public List<HashMap<String, String>> getEnergyDashboardDetails() { 
		String returnList = "Hello";
		List<ACDetails> acDataList = new ArrayList<ACDetails>();
		acDataList = acDetailsRepo.findAll();
		List<HashMap<String ,String>> energyList = new ArrayList<HashMap<String ,String>> ();
		for (ACDetails acData: acDataList) {
			HashMap<String ,String> map=new HashMap<String,String>();
			List<ACTimeSeries> acUsageList = acDataRepo.getACState(acData.getAc_id());
			EnergyCalcUtils energyCalc =  new EnergyCalcUtils();
			map = energyCalc.modeCount(acUsageList, acData.getAc_id());
			energyList.add(energyCalc.calculateEnergy(acData, map));
			
		}
			
		return energyList;
	}
	
	@GetMapping("/get_pred_ac_state")
	public Object[] getACState(){
		Mono<Object[]> response= webClientBuilder.build() 
				.get()
				.uri("http://127.0.0.1:8000/api/v1/get_ac_pred")
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToMono(Object[].class)
				.log();
				Object[] objects = response.block();
				//System.out.println(objects[0].toString());
					
				return objects;
		
	}
	@GetMapping("/get_prediction_energy")
	public HashMap<String, Double> getACStateEnergy(){
		EnergyCalcUtils energyCalc = new EnergyCalcUtils();
		List<ACDetails> acDataList = new ArrayList<ACDetails>();
		Mono<Object[]> response= webClientBuilder.build()
				.get()
				.uri("http://127.0.0.1:8000/api/v1/get_ac_pred")
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToMono(Object[].class)
				.log();
				Object[] objects = response.block();
				//System.out.println(objects[0].toString());
		
		acDataList = acDetailsRepo.findAll();
		
		
		HashMap<String, Double> energyData = energyCalc.CalculateFutureEnergy(objects, acDataList);
		return energyData;
		
	}


	
}
