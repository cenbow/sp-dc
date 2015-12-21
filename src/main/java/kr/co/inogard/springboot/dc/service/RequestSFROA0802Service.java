package kr.co.inogard.springboot.dc.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import kr.co.inogard.springboot.dc.domain.OpenapiBatchJobMapperDomain;
import kr.co.inogard.springboot.dc.domain.OpenapiBatchJobMapperDomainKey;
import kr.co.inogard.springboot.dc.domain.ResponseFileDomain;
import kr.co.inogard.springboot.dc.domain.ResponseSFROA0802Domain;
import kr.co.inogard.springboot.dc.external.domain.ExternalResponseFileDomain;
import kr.co.inogard.springboot.dc.external.domain.ExternalResponseSFROA0802Domain;
import kr.co.inogard.springboot.dc.repository.OpenapiBatchJobMapperRepository;

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
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.Assert;

@Service
public class RequestSFROA0802Service {
	
	private static final Logger log = LoggerFactory.getLogger(RequestSFROA0802Service.class);
	
	private static final int openApiId = 1;
	
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
	private OpenapiBatchJobMapperRepository openapiBatchJobMapperRepository;
	
	@Autowired
	@Qualifier("datasourceTwoEntityManager")
	private EntityManagerFactory datasourceTwoEntityManager;
	
	public void run(String sDate, String eDate, String orderCode) throws Exception{

		Assert.notNull(sDate, "'sDate' must not be null");
		Assert.notNull(eDate, "'eDate' must not be null");
		Assert.notNull(orderCode, "'orderCode' must not be null");
		
//		String groupId	= new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		
		Properties prop = new Properties();
//		prop.setProperty("groupId", groupId);
		prop.setProperty("sDate", sDate);
		prop.setProperty("eDate", eDate);
		prop.setProperty("orderCode", orderCode);
		prop.setProperty("date", new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()));
		
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
        
        // Step1 구성 시작(데이터 가져오기)
        Step requestSFROA802TaskletStep = stepBuilderFactory.get("requestSFROA802")
        		.tasklet(requestSFROA802Tasklet)
        		.transactionManager(datasourceOneTransactionManager)
        		//.startLimit(3)	// 오류 발생 시 3번만 재시도
        		//.allowStartIfComplete(true)	// 작업 재시작 시 현재 Step도 다시 실행할지
        		.build();
        // Step1 구성 끝
        
        // Step2 구성 시작(가져온 데이터를 타 DB로 복사하기)
        JpaItemWriter<ExternalResponseSFROA0802Domain> responseSFROA0802Writer = new JpaItemWriter();
        responseSFROA0802Writer.setEntityManagerFactory(datasourceTwoEntityManager);
        
        ItemProcessor<ResponseSFROA0802Domain, ExternalResponseSFROA0802Domain> responseSFROA0802Processor = new ResponseSFROA0802ItemProcessor();
        
        Step responseSFROA0802DomainTransferStep = ((SimpleStepBuilder<ResponseSFROA0802Domain, ExternalResponseSFROA0802Domain>) stepBuilderFactory.get("responseSFROA0802DomainTransfer")
        		.<ResponseSFROA0802Domain, ExternalResponseSFROA0802Domain> chunk(200) // 읽기/쓰기 단위
                .transactionManager(datasourceTwoTransactionManager))
                .reader(responseSFROA0802ItemReader)
                .writer(responseSFROA0802Writer)
                .processor(responseSFROA0802Processor)
                .listener(responseSFROA0802ItemWriterListener)
                .build();
        // Step2 구성 끝
        
        // Step3 구성 시작(가져온 데이터 중 파일정보를 타 DB로 복사하기)
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
        
        // Step4 구성 시작(오류가 발생하면 E-Mail로 알림)
        Step responseSFROA802ErrorMailSendStep = stepBuilderFactory.get("responseSFROA802ErrorMailSend")
        		.tasklet(responseSFROA802ErrorMailSendTasklet)
        		.build();
        // Step4 구성 끝
        
        // Job에 Step 결합
        Job job = jobBuilderFactory.get("SFROA0802")
        		.listener(responseSFROA0802JobExecutionListener)
        		.start(requestSFROA802TaskletStep)
        		.next(responseSFROA0802DomainTransferStep)
        		.next(responseFileDomainTransferStep)
        		.on(FlowExecutionStatus.FAILED.getName())
        		.to(responseSFROA802ErrorMailSendStep)
        		.end()
        		.build();
        
        // 실행
		JobExecution execution = simpleJobLauncher.run(job, jobParameters);
		OpenapiBatchJobMapperDomain openapiBatchJobMapperDomain = new OpenapiBatchJobMapperDomain();
		openapiBatchJobMapperDomain.setJobExecutionId(execution.getId());
		openapiBatchJobMapperDomain.setOpenApiId(openApiId);
		
		openapiBatchJobMapperRepository.save(openapiBatchJobMapperDomain);
	}
}
