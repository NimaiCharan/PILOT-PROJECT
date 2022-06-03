package com.iqnext.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import com.iqnext.model.ACDetails;
import com.iqnext.model.ACTimeSeries;

public class EnergyCalcUtils {
	
	
	public HashMap<String, String> modeCount(List<ACTimeSeries> acUsageList, String ac_id) {
		int mode1 = 0;
		int mode2 = 0;
		int mode3 = 0;
		int mode4 = 0;
		int mode5 = 0;
		double test;
		HashMap<String ,String> map=new HashMap<String,String>();
		for(ACTimeSeries acDataT: acUsageList) {
			
			if(acDataT.getAc_mode()==1) {
				mode1++;
			}
			else if(acDataT.getAc_mode()==2) {
				mode2++;
			}
			else if(acDataT.getAc_mode()==3) {
				mode3++;
			}
			else if(acDataT.getAc_mode()==4) {
				mode4++;
			}
			else if(acDataT.getAc_mode()==5) {
				mode5++;
			}
			
		}
			int count = acUsageList.size();
			double totalRunHour = count/2;
			
			map.put("mode1", String.valueOf(mode1));
			map.put("mode2", String.valueOf(mode2));
			map.put("mode3", String.valueOf(mode3));
			map.put("mode4", String.valueOf(mode4));
			map.put("mode5", String.valueOf(mode5));
			
			
			map.put("totalHour", String.valueOf(totalRunHour));
			//map.put("energyConsumed", String.valueOf(energyConsumed));
			map.put("ac_id", ac_id);
			
			
			//test = (energyWithoutMode);
		
		
		return map;
		
	}



	public double calculateEnergy(int modeCount, ACDetails acData, int mode  ) {
		float energy1 = (acData.getAc_power_consumption()* (modeCount/2))/1000;
		float eer = (acData.getAc_cooling_capacity()/acData.getAc_power_consumption());
		float energyWithoutMode = (((acData.getAc_cooling_capacity()* acData.getAc_capacity())/eer)*(modeCount/2)/1000);
		double energy2 =  0.0;
		if(mode==1) {
			energy2 = energyWithoutMode*1.15;
		}
		else if(mode==2) {
			energy2 = energyWithoutMode*1.125;
		}
		else if(mode==3) {
			energy2 = energyWithoutMode;
		}
		else if(mode==4) {
			energy2 = energyWithoutMode*1.1;
		}
		else if(mode==5) {
			energy2 = energyWithoutMode*1.15;
		}
		double energyConsumed = (energy1+energy2)/2;
		
		return energyConsumed;
	}



	public HashMap<String, String> calculateEnergy(ACDetails acData, HashMap<String, String> map) {
		float time = Float.parseFloat(map.get("totalHour"));
		float energy1 = ((acData.getAc_power_consumption())*Float.parseFloat(map.get("totalHour"))/1000);
//		float eer = (acData.getAc_cooling_capacity()/acData.getAc_power_consumption());
//		float energyWithoutMode = (((acData.getAc_cooling_capacity()/1000)* acData.getAc_capacity())/eer);
//		double tempEnergy =  (energyWithoutMode*1.15)/(Float.parseFloat(map.get("mode1"))/2)
//							+ (energyWithoutMode*1.15)/(Float.parseFloat(map.get("mode2"))/2)
//							+(energyWithoutMode*1.15)/(Float.parseFloat(map.get("mode3"))/2)
//							+(energyWithoutMode*1.15)/(Float.parseFloat(map.get("mode4"))/2)
//							+(energyWithoutMode*1.15)/(Float.parseFloat(map.get("mode5"))/2);
//		double energyConsumed = (energy1+tempEnergy)/2;
		System.out.println(energy1);
		map.put("energyConsumed", String.valueOf(energy1));
		return map;
	}
	
	public HashMap<String, Double> CalculateFutureEnergy(Object[] objects, List<ACDetails> acDataList) {
		HashMap<String, Double> energyMap= new HashMap<String,Double>();
		int zeroCount = 0;
		int nonZeroCount = 0;
		for(Object obj:objects) {
			
			LinkedHashMap<?, ?> map = (LinkedHashMap<?, ?>) obj;
			//System.out.println(map);
			if(map.get("ac_status").toString().equals("0")) {
				zeroCount++;
			}
			else {
				nonZeroCount++;
			}
		}
		int totalCount = objects.length;
		float turnOnPercent =(float) nonZeroCount/totalCount;
		
		System.out.println("ZERO: "+zeroCount+" NON ZERO: "+nonZeroCount);
		System.out.println("PERCENT: "+turnOnPercent+" TOTAL COUNT: "+totalCount);
		
		double totalEnergConsuption = 0.0;
		for(ACDetails acData: acDataList) {
			double power = (double) acData.getAc_power_consumption();
			double poweConsuption = (power*24*turnOnPercent)/1000;
			totalEnergConsuption+=poweConsuption;
			energyMap.put(acData.getAc_id(), poweConsuption);
		}
		energyMap.put("TOTAL", totalEnergConsuption);
		return energyMap;
	}
	
	
	
}
