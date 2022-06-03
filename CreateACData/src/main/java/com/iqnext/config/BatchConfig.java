package com.iqnext.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.iqnext.constants.JobConstants;
import com.iqnext.repo.ACDataRepo;
import com.iqnext.repo.ACDetailsRepo;
import com.iqnext.repo.WeatherDataRepo;
import com.iqnext.tasklet.ACData;

@EnableBatchProcessing
@Configuration
public class BatchConfig {
	
	public static Logger LOG = LoggerFactory.getLogger(BatchConfig.class);
	
	@Autowired
    private JobBuilderFactory jobBuilders;

    @Autowired
    private StepBuilderFactory stepBuilders;
    
    @Autowired 
	WeatherDataRepo weatherDataRepo;
    
    @Autowired
    ACDataRepo acDataRepo;
    
    @Autowired
    ACDetailsRepo acDetailsRepo;
    
   
    @Value("${weather.data.fromday}")
	private String fromDay;
	
	@Value("${weather.data.today}")
	private String toDay;
	
	JobConstants constants;
	
    
    @Bean
    public Job createACDataJob() {
    	//constants = new JobConstants(fromDay, toDay);
    	LOG.info("CREATE AC DETAILS JOBS STARTED SUCCESSFULLY....");
    	//constants.getFromDay();
    	return jobBuilders.get("CreateACData JOB")
    			.incrementer(new RunIdIncrementer())
    			.start(addACDetailsSteps())
    			.build();
    			
    	
    }
    
    
    @Bean
    public Step addACDetailsSteps() {
    	return stepBuilders.get("AddACDetails")
    			.tasklet(new ACData(weatherDataRepo, acDataRepo,acDetailsRepo, fromDay, toDay))
    			.allowStartIfComplete(true)
    			.build();
    }

}
