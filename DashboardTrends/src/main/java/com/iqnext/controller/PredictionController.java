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
	
	@GetMapping("/get_ac_usage_perDay")
	public List<HashMap<String, List<Object[]>>> dayWiseACUsage() {
		List<ACDetails> acDataList = new ArrayList<ACDetails>();
		acDataList = acDetailsRepo.findAll();
		
		List<HashMap<String ,List<Object[]>>> energyList = new ArrayList<HashMap<String ,List<Object[]>>> ();
		for (ACDetails acData: acDataList) {
			//HashMap<String ,String> map=new HashMap<String,String>();
			HashMap<String ,List<Object[]>> map=new HashMap<String,List<Object[]>>();
			List<Object[]>  acUsageList = acDataRepo.getDayWiseResult(acData.getAc_id());
			map.put(acData.getAc_id(), acUsageList);
			energyList.add(map);
		}
			
		return energyList;
	}
	@GetMapping("/get_energy_usage_perDay")
	public List<HashMap<String, HashMap<String, Double>>> dayWiseEnergyUsage() {
		List<ACDetails> acDataList = new ArrayList<ACDetails>();
		acDataList = acDetailsRepo.findAll();
		
		List<HashMap<String ,HashMap<String ,Double>>> energyList = new ArrayList<HashMap<String ,HashMap<String ,Double>>> ();
		for (ACDetails acData: acDataList) {
			//HashMap<String ,String> map=new HashMap<String,String>();
			HashMap<String ,HashMap<String ,Double>> map=new HashMap<String,HashMap<String ,Double>>();
			List<Object[]>  acUsageList = acDataRepo.getDayWiseResult(acData.getAc_id());
			System.out.println(acUsageList);
			HashMap<String ,Double> mapData=new HashMap<String,Double>();
			for(Object[] obj:acUsageList) {
				
				int time = Integer.parseInt(obj[1].toString());
				double energy= (acData.getAc_power_consumption() * (time/2) )/1000;
				mapData.put(obj[0].toString(), energy);
				
			}
			System.out.println(mapData);
			map.put(acData.getAc_id(), mapData);
			energyList.add(map);
		}
		System.out.println(energyList);
		return energyList;
	}

	
}
