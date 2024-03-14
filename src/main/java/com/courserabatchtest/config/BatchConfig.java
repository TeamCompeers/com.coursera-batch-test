package com.courserabatchtest.config;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration

public class BatchConfig {
	
	private final JobLauncher jobLauncher;
	private final JobRepository jobRepository;
	private final PlatformTransactionManager batchTransactionManager;
	public static final int BATCH_SIZE=5;
	
	public BatchConfig(JobLauncher jobLauncher, JobRepository jobRepository, PlatformTransactionManager batchTransactionManager)
	{
		this.jobLauncher=jobLauncher;
		this.jobRepository=jobRepository;
		this.batchTransactionManager=batchTransactionManager;
	}
	
	public static final Logger logger =LoggerFactory.getLogger(BatchConfig.class);
	
	@Bean
	public Job firstJob()
	{
		return  new JobBuilder("first job", jobRepository)
				.incrementer(new RunIdIncrementer())
				.start(chunkStep())
				.next(taskletStep())
				.build();
	}
	@Bean
	public Step taskletStep()
	{
		return new StepBuilder("first step", jobRepository)
				.tasklet((stepContribution, chunckContext) -> {
					System.out.println("This is first tasklet step ");
					System.out.println("SEC={}"+chunckContext.getStepContext().getStepExecutionContext());
					return RepeatStatus.FINISHED;
				},batchTransactionManager).build();
				
	}
	
	@Bean
	public Step chunkStep() {
		return new StepBuilder("first step", jobRepository)
				.<String,String>chunk(BATCH_SIZE, batchTransactionManager)
				.reader(reader())
				.writer(writer())
				.build();
		
	}
	
	@Bean
	public ItemReader<String>reader()
	{
		List<String> data = Arrays.asList("Byte","code","disk");
		return new ListItemReader<>(data);
	}
	
	@Bean
	public ItemWriter<String>writer(){
		return items->{
			for(var item:items) {
				System.out.println("Writing item:{}"+item);
			}
		};
	}
	
	
		
}
