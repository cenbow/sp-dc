package kr.co.inogard.springboot.dc.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import kr.co.inogard.springboot.dc.domain.ResponseFileDomain;
import kr.co.inogard.springboot.dc.domain.ResponseSFROA0802Domain;
import kr.co.inogard.springboot.dc.external.domain.ExternalResponseFileDomain;
import kr.co.inogard.springboot.dc.external.domain.ExternalResponseSFROA0802Domain;
import kr.co.inogard.springboot.dc.repository.RequestSFROA0802Repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.converter.DefaultJobParametersConverter;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.Assert;

@Service
public class RequestSFROA0802Service {
	
	private static final Logger log = LoggerFactory.getLogger(RequestSFROA0802Service.class);
	
	@Autowired
	@Qualifier("datasourceOneTransactionManager")
	private PlatformTransactionManager datasourceOneTransactionManager;
	
	@Autowired
	@Qualifier("datasourceTwoTransactionManager")
	private PlatformTransactionManager datasourceTwoTransactionManager;
	
	@Autowired
	@Qualifier("datasourceOneDataSource")
	private DataSource dataSource;
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
 
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private RequestSFROA802Tasklet requestSFROA802Tasklet;
	
	@Autowired
	@Qualifier("responseSFROA0802ItemReader")
	private ResponseSFROA0802ItemReader responseSFROA0802ItemReader;
	
	@Autowired
	private ResponseSFROA0802ItemWriterListener responseSFROA0802ItemWriterListener;

	@Autowired
	private ResponseSFROA0802JobExecutionListener responseSFROA0802JobExecutionListener;
	
	@Autowired
	private ResponseFileItemReader responseFileItemReader;
	
	@Autowired
	private ResponseFileItemProcessor responseFileItemProcessor;
	
	@Autowired
	private ResponseSFROA802ErrorMailSendTasklet responseSFROA802ErrorMailSendTasklet;
	
	@Autowired
	@Qualifier("datasourceTwoEntityManager")
	private EntityManagerFactory datasourceTwoEntityManager;
	
	public void run(String sDate, String eDate, String orderCode) throws Exception{

		Assert.notNull(sDate, "'sDate' must not be null");
		Assert.notNull(eDate, "'eDate' must not be null");
		Assert.notNull(orderCode, "'orderCode' must not be null");
		
		String groupId	= new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		
		Properties prop = new Properties();
		prop.setProperty("groupId", groupId);
		prop.setProperty("sDate", sDate);
		prop.setProperty("eDate", eDate);
		prop.setProperty("orderCode", orderCode);
		
		JobParametersConverter jobParametersConverter = new DefaultJobParametersConverter();
		JobParameters jobParameters = jobParametersConverter.getJobParameters(prop);
		
		JobRepositoryFactoryBean jobRepositoryFactoryBean = new JobRepositoryFactoryBean();
		jobRepositoryFactoryBean.setTransactionManager(datasourceOneTransactionManager);
		jobRepositoryFactoryBean.setDataSource(dataSource);
		JobRepository jobRepository = jobRepositoryFactoryBean.getObject();
		
		SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor();
		
		SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();
        simpleJobLauncher.setJobRepository(jobRepository);
        simpleJobLauncher.setTaskExecutor(simpleAsyncTaskExecutor);
        simpleJobLauncher.afterPropertiesSet();
        
        // Step1 구성 시작
        Step requestSFROA802TaskletStep = stepBuilderFactory.get("requestSFROA802")
        		.tasklet(requestSFROA802Tasklet)
        		.transactionManager(datasourceOneTransactionManager)
        		.build();
        // Step1 구성 끝
        
        // Step3 구성 시작
        ItemReader listFileItemReader = responseFileItemReader;
        
        AsyncItemProcessor asyncItemProcessor = new AsyncItemProcessor();
        asyncItemProcessor.setDelegate(responseFileItemProcessor);
        asyncItemProcessor.setTaskExecutor(simpleAsyncTaskExecutor);
        
        AsyncItemWriter asyncItemWriter = new AsyncItemWriter();
    	JpaItemWriter<ExternalResponseFileDomain> writer = new JpaItemWriter();
		writer.setEntityManagerFactory(datasourceTwoEntityManager);
		asyncItemWriter.setDelegate(writer);
        
        Step responseFileDomainTransferStep = ((SimpleStepBuilder<ResponseFileDomain, ExternalResponseFileDomain>) stepBuilderFactory.get("responseFileDomainTransfer")
                .<ResponseFileDomain, ExternalResponseFileDomain> chunk(200) // 읽기/쓰기 단위
                .transactionManager(datasourceTwoTransactionManager))
                .reader(listFileItemReader)
                .writer(asyncItemWriter)
                .processor(asyncItemProcessor)
                .build();
        // Step3 구성 끝
        
        // Step4 구성 시작
        Step responseSFROA802ErrorMailSendStep = stepBuilderFactory.get("responseSFROA802ErrorMailSend")
        		.tasklet(responseSFROA802ErrorMailSendTasklet)
        		.build();
        // Step4 구성 끝
        
        Job job = jobBuilderFactory.get("SFROA0802")
        		.listener(responseSFROA0802JobExecutionListener)
        		.start(requestSFROA802TaskletStep)
        		.next(this.responseSFROA0802DomainTransferStep())
        		.next(responseFileDomainTransferStep)
        		.on(FlowExecutionStatus.FAILED.getName())
        		.to(responseSFROA802ErrorMailSendStep)
        		.end()
        		.build();
        
		JobExecution execution = simpleJobLauncher.run(job, jobParameters);
	}
	
	@Bean
	public Step responseSFROA0802DomainTransferStep() throws Exception {
		return ((SimpleStepBuilder<ResponseSFROA0802Domain, ExternalResponseSFROA0802Domain>) stepBuilderFactory.get("responseSFROA0802DomainTransfer")
                .<ResponseSFROA0802Domain, ExternalResponseSFROA0802Domain> chunk(200) // 읽기/쓰기 단위
                .transactionManager(datasourceTwoTransactionManager))
                .reader(responseSFROA0802ItemReader)
                .writer(responseSFROA0802Writer())
                .processor(responseSFROA0802Processor())
                .listener(responseSFROA0802ItemWriterListener)
                .build();
	}
	
	@Bean
	public JpaItemWriter<ExternalResponseSFROA0802Domain> responseSFROA0802Writer() {
    	JpaItemWriter writer = new JpaItemWriter();
		writer.setEntityManagerFactory(datasourceTwoEntityManager);
	    return writer;
	}
	
	@Bean
	public ItemProcessor<ResponseSFROA0802Domain, ExternalResponseSFROA0802Domain> responseSFROA0802Processor() {
        return new ResponseSFROA0802ItemProcessor();
    }
}
