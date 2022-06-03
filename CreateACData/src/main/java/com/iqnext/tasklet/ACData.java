package com.iqnext.tasklet;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import com.iqnext.constants.JobConstants;
import com.iqnext.model.ACDetails;
import com.iqnext.model.ACTimeSeries;
import com.iqnext.model.WeatherData;
import com.iqnext.repo.ACDataRepo;
import com.iqnext.repo.ACDetailsRepo;
import com.iqnext.repo.WeatherDataRepo;

public class ACData implements Tasklet {

	public static Logger LOG = LoggerFactory.getLogger(ACData.class);
	public static final String RESULT_FORMAT = "0.00";
	public static final String  HOTORNOT[] ={"EXTREME COLD", "COLD", "NORMAL", "HOT", "TOO HOT TO HANDLE"};
	public static final String  AC_STATUS[] = {"OFF", "AUTO", "COOL", "FAN", "DRY", "HEAT" };
	public WeatherDataRepo weatherDataRepo;
	public ACDataRepo acDataRepo;
	public ACDetailsRepo acDetailsRepo;
	public JobConstants constants;
	private int fromDay;
	
	private int toDay;
	
	
	public ACData(WeatherDataRepo weatherDataRepo, ACDataRepo acDataRepo, ACDetailsRepo acDetailsRepo, String fromDay, String toDay) {
		this.weatherDataRepo = weatherDataRepo;
		this.acDataRepo = acDataRepo;
		this.acDetailsRepo = acDetailsRepo;
		this.fromDay =Integer.parseInt(fromDay);
		this.toDay = Integer.parseInt(toDay);
	}
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// TODO Auto-generated method stub
		// LOG.info("FromDAY YY: "+fromDay);

		LOG.info("FromDAY : " + fromDay);

		List<WeatherData> weatherDataList = weatherDataRepo.dayWiseRecords(fromDay, toDay);
		List<ACDetails> acDetailsList = acDetailsRepo.findAll();

		try {
			for (ACDetails acDetails : acDetailsList) {
				for (WeatherData weatherData : weatherDataList) {

					ACTimeSeries acData = new ACTimeSeries();
					acData.setDate(weatherData.getDate());
					acData.setTime(weatherData.getTime());

					acData.setAc_Id(acDetails.getAc_id());
					// LOG.info("AC ID: "+acDetails.getAc_id());
					int ac_status = hotOrNot(weatherData);
					acData.setAc_mode(ac_status);
					// LOG.info("DATE : "+weatherData.getDate());
					acDataRepo.save(acData);
					LOG.info("AC TIME SERIES DATA SAVED SUCCESSFULLY");
				}
			}
		} catch (Exception e) {
			LOG.error(e.toString());
		}

		// LOG.info("List"+weatherDataRepo.findAll());
		return null;
	}
	
	
	public int hotOrNot(WeatherData weather) {
		
		
		int rh = weather.getHumidity();
		double F = ((Float.parseFloat(weather.getTemp())*9)/5)+32; 
		double Hindex;
		Hindex = -42.379 + 2.04901523 * F + 10.14333127 * rh;
        Hindex = Hindex - 0.22475541 * F * rh - 6.83783 * Math.pow(10, -3) * F * F;
        Hindex = Hindex - 5.481717 * Math.pow(10, -2) * rh * rh;
        Hindex = Hindex + 1.22874 * Math.pow(10, -3) * F * F * rh;
        Hindex = Hindex + 8.5282 * Math.pow(10, -4) * F * rh * rh;
        Hindex = Hindex - 1.99 * Math.pow(10, -6) * F * F * rh * rh;
        Hindex = ((Hindex-32)*5)/9;
        DecimalFormat df = new DecimalFormat(RESULT_FORMAT);
        df.setRoundingMode(RoundingMode.HALF_UP);
        double heatIndexC =  Double.parseDouble(df.format(Hindex));
        
      
        double avg_temp =(Float.parseFloat(weather.getFeels_like())+heatIndexC)*0.5;
        int hotOrNotStatus = -1;
        if(avg_temp<20) {
        	hotOrNotStatus = 0;
        }
        else if(avg_temp<30 && avg_temp >=20) {
        	hotOrNotStatus = 1;
        }
        else if(avg_temp<37 && avg_temp >=30) {
        	hotOrNotStatus = 2;
        }
        else if(avg_temp<45 && avg_temp >=37) {
        	hotOrNotStatus = 3;
        }
        else if(avg_temp >=45) {
        	hotOrNotStatus = 4;
        }
        int ac_status = AC_Status(hotOrNotStatus, weather.getTime());
        
	//	LOG.info("TEMP: "+weather.getTemp()+" FEELS LIKE: "+ HOTORNOT[hotOrNotStatus] +"  AC_STATUS: "+AC_STATUS[ac_status]);
		return ac_status;
	
	}
	
	public int AC_Status(int hotOrNot, String time) {
		int AC_status = -1;
		int workTime;
		String []timePart=time.split(" ");
		if(timePart[1].equals("PM"))
			workTime = ((Integer.parseInt(timePart[0].split(":")[0]))+12);
			
		else {
			workTime = Integer.parseInt(timePart[0].split(":")[0]);
		}
		if(workTime==24) {
			workTime=0;
		}
		//LOG.info("WORK TIME: "+workTime+ " TIME: "+time);
		if(workTime>=3 && workTime<5) {
			if(hotOrNot == 0) {
				int state[] = {0,1,5};
				AC_status = getACStatusRand(state);
			}
			else if(hotOrNot==1) {
				int state[] = {0,1,5};
				AC_status = getACStatusRand(state);
			}
			else if(hotOrNot==2) {
				int state[] = {0,1,2,3};
				AC_status = getACStatusRand(state);
			}
			else if(hotOrNot==3) {
				int state[] = {0,1,2};
				AC_status = getACStatusRand(state);
			}
			else if(hotOrNot==4) {
				int state[] = {1,2};
				AC_status = getACStatusRand(state);
			}
		}else if(workTime>5.1 && workTime<15.1){
			if(hotOrNot == 0) {
				int state[] = {1,3,5};
				AC_status = getACStatusRand(state);
			}
			else if(hotOrNot==1) {
				int state[] = {1,3,5};
				AC_status = getACStatusRand(state);
			}
			else if(hotOrNot==2) {
				int state[] = {1,2,3};
				AC_status = getACStatusRand(state);
			}
			else if(hotOrNot==3) {
				int state[] = {1,2};
				AC_status = getACStatusRand(state);
			}
			else if(hotOrNot==4) {
				int state[] = {1,2};
				AC_status = getACStatusRand(state);
			}
		}else{
			//LOG.info("OFF TIME: "+ workTime);
			AC_status = 0;
			//LOG.info("HELLO");
		}
		return AC_status;
	}
	public int getACStatusRand(int []n) {
		
		int size = n.length;
		Random rand = new Random();
		int index = rand.nextInt(size);
		return n[index];
	}

}
