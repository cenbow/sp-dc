package kr.co.inogard.springbiit.dc.controller;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Future;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import kr.co.inogard.springboot.dc.Application;
import kr.co.inogard.springboot.dc.domain.OpenAPIRequest;
import kr.co.inogard.springboot.dc.domain.RequestSFROA0802;
import kr.co.inogard.springboot.dc.domain.RequestSFROA0802Domain;
import kr.co.inogard.springboot.dc.domain.RequestSFROA0802DomainKey;
import kr.co.inogard.springboot.dc.domain.Response;
import kr.co.inogard.springboot.dc.domain.ResponseFileDomain;
import kr.co.inogard.springboot.dc.domain.ResponseSFROA0802;
import kr.co.inogard.springboot.dc.domain.ResponseSFROA0802Domain;
import kr.co.inogard.springboot.dc.external.domain.ExternalResponseFileDomain;
import kr.co.inogard.springboot.dc.external.domain.ExternalResponseSFROA0802Domain;
import kr.co.inogard.springboot.dc.repository.RequestSFROA0802Repository;
import kr.co.inogard.springboot.dc.service.AnnStdDocAsyncDownloadService;
import kr.co.inogard.springboot.dc.service.OpenAPIContext;
import kr.co.inogard.springboot.dc.service.OpenAPIRequestService;
import kr.co.inogard.springboot.dc.service.Paging;
import kr.co.inogard.springboot.dc.service.ResponseFileItemProcessor;
import kr.co.inogard.springboot.dc.service.ResponseFileItemReader;
import kr.co.inogard.springboot.dc.service.ResponseSFROA0802ItemProcessor;
import kr.co.inogard.springboot.dc.service.ResponseSFROA0802ItemReader;
import kr.co.inogard.springboot.dc.service.ResponseSFROA0802ItemWriterListener;
import kr.co.inogard.springboot.dc.service.ResponseSFROA0802JobExecutionListener;
import kr.co.inogard.springboot.dc.utils.FileUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.converter.DefaultJobParametersConverter;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.ReflectionUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class DataGovControllerTest {
	
	private static final Logger log = LoggerFactory.getLogger(DataGovControllerTest.class);
	
	@Value("${agent.root}")
	private String agentRootDirectory;
	
	@Autowired
	private RequestSFROA0802Repository requestSFROA0802Repository;
	
	@Autowired
	private JpaRepository responseSFROA0802Repository;
	
	@Autowired
	private JpaRepository responseFileRepository;
	
	@Autowired
	private OpenAPIRequestService openAPIRequestService;
	
	@Autowired
	private AnnStdDocAsyncDownloadService annStdDocAsyncDownloadService;
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
 
	@Autowired
	private StepBuilderFactory stepBuilderFactory;

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
	@Qualifier("datasourceTwoEntityManager")
	private EntityManagerFactory datasourceTwoEntityManager;
	
	@Autowired
	@Qualifier("responseSFROA0802ItemReader")
	private ResponseSFROA0802ItemReader responseSFROA0802ItemReader;

	@Autowired
	private ResponseSFROA0802ItemWriterListener responseSFROA0802ItemWriterListener;
	
	@Autowired
	private ResponseSFROA0802JobExecutionListener responseSFROA0802JobExecutionListener;
	
	@Autowired
	@Qualifier("responseFileItemReader")
	private ResponseFileItemReader responseFileItemReader;
	
	@Autowired
	private ResponseFileItemProcessor responseFileItemProcessor;

	@Test
	public void getData() throws Exception {
		
		// 업무 트렌젝션(DB아님) 단위의 키로 사용할 값을 생성하여 OpenAPIContext에 담아서 같은 값을 사용할 수 있도록 함.
		String timestamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		OpenAPIRequest openAPIRequest = new OpenAPIRequest();
		openAPIRequest.setGroupId(timestamp);
		openAPIRequest.setRequestSeq(1);
		OpenAPIContext.set(openAPIRequest);
		
//		RequestSFROA0802 requestSFROA0802 = this.getBeanInstance(RequestSFROA0802.class);
		
		String subUrl = "BidPublicInfoService/getInsttAcctoBidPblancListThng";
		
		int pageSize 	= 200;
		int pageNo 		= 1;
		
		RequestSFROA0802 request = new RequestSFROA0802();
		request.setGroupId(timestamp);
		request.setRequestSeq(1);
		request.setNumOfRows(pageSize);
		request.setPageNo(pageNo);
		request.setSDate("20150401");
		request.setEDate("20150420");
		request.setOrderCode("한국공항공사");
		
		List<ResponseSFROA0802> listResponse = new ArrayList();
		
		Response response = this.getDataFromOpenAPI(subUrl, request, listResponse);
		log.debug("TotalResultCount : " + listResponse.size());
		
		List<ResponseFileDomain> listDownloadFileCandidate = new ArrayList<>();
		for(ResponseSFROA0802 responseSFROA0802 : listResponse){
			
			ResponseSFROA0802Domain responseSFROA0802Domain = new ResponseSFROA0802Domain();
			BeanUtils.copyProperties(responseSFROA0802, responseSFROA0802Domain);
			
			responseSFROA0802Domain.setTransferYn("N");
			responseSFROA0802Repository.save(responseSFROA0802Domain);
			if(null != responseSFROA0802.getAnnStdDoc1()
					&& !"".equals(responseSFROA0802.getAnnStdDoc1())){
				
				ResponseFileDomain responseFileDomain = new ResponseFileDomain();
				responseFileDomain.setUrl(responseSFROA0802.getAnnStdDoc1());
				
				responseFileRepository.save(responseFileDomain);
				
				listDownloadFileCandidate.add(responseFileDomain);
			}
		}
		responseFileRepository.flush();
		responseSFROA0802Repository.flush();
		
//		if(listDownloadFileCandidate.size() > 0){
//			try {
//				log.debug("#############################");
//				log.debug("비동기 호출 시작");
//				log.debug("#############################");
//				
//				for(ResponseFileDomain responseFileDomain : listDownloadFileCandidate){
//					Future<ResponseFileDomain> future = annStdDocAsyncDownloadService.download(responseFileDomain);
//					if(future.isDone()){
//						log.debug("######## 비동기 작업 완료 ########");
//					}
//				}
//				log.debug("#############################");
//				log.debug("비동기 호출 끝");
//				log.debug("#############################");
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
		
		if(listResponse.size() > 0){
			// Batch 시작
			Properties prop = new Properties();
			prop.setProperty("groupId", timestamp);
			
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
	        
	        List<Step> listStep = new ArrayList<>();
	        listStep.add(step1());
	        if(listDownloadFileCandidate.size() > 0){
		        ListItemReader<ResponseFileDomain> listItemReader = new ListItemReader<>(listDownloadFileCandidate);
		        
		        AsyncItemProcessor asyncItemProcessor = new AsyncItemProcessor();
		        asyncItemProcessor.setDelegate(responseFileItemProcessor);
		        asyncItemProcessor.setTaskExecutor(simpleAsyncTaskExecutor);
		        
		        AsyncItemWriter asyncItemWriter = new AsyncItemWriter();
		    	JpaItemWriter<ExternalResponseFileDomain> writer = new JpaItemWriter();
				writer.setEntityManagerFactory(datasourceTwoEntityManager);
				asyncItemWriter.setDelegate(writer);
		        
		        Step step = ((SimpleStepBuilder<ResponseFileDomain, ExternalResponseFileDomain>) stepBuilderFactory.get("responseFileDomainTransfer")
		                .<ResponseFileDomain, ExternalResponseFileDomain> chunk(100) // 읽기/쓰기 단위
		                .transactionManager(datasourceTwoTransactionManager))
		                .reader(listItemReader)
		                .writer(asyncItemWriter)
		                .processor(asyncItemProcessor)
		                .chunk(10)
		                .build();
		        
		        listStep.add(step);
	        }
	        
	        JobExecutionListener[] arrListener = {responseSFROA0802JobExecutionListener};
	        
//	        Job job = this.job();
	        SimpleJob job = new SimpleJob();
	        job.setName("responseSFROA0802ExportToExternal");
	        job.setJobExecutionListeners(arrListener);
	        job.setSteps(listStep);
	        job.setJobRepository(jobRepository);
	        
	        JobExecution execution = simpleJobLauncher.run(job(), jobParameters);
	        log.debug("execution.getId() = " + execution.getId());
	        log.debug("Exit Status : " + execution.getStatus());
	        
	        
	        openAPIRequest = OpenAPIContext.get();
			RequestSFROA0802DomainKey id = new RequestSFROA0802DomainKey();
			id.setGroupId(openAPIRequest.getGroupId());
			id.setRequestSeq(openAPIRequest.getRequestSeq());
			
			RequestSFROA0802Domain requestSFROA0802Domain = requestSFROA0802Repository.findOne(id);
	        requestSFROA0802Domain.setJobExecutionId(execution.getId());
	        requestSFROA0802Domain.setJobExecutionStatus(execution.getStatus().toString());
	        requestSFROA0802Repository.saveAndFlush(requestSFROA0802Domain);
		}
		
//		Thread.sleep(30000);
		
		// 사용이 끝나면 삭제하기
		OpenAPIContext.reset();
		
		log.debug("#############################");
		log.debug("업무 끝");
		log.debug("#############################");	
	}
	
	public Response getDataFromOpenAPI(String subUrl, RequestSFROA0802 request, List<ResponseSFROA0802> listResponse) throws Exception{
		
		RequestSFROA0802Domain requestSFROA0802Domain = new RequestSFROA0802Domain();
		BeanUtils.copyProperties(request, requestSFROA0802Domain);
		
		// 조회조건 저장
		log.debug("RequestSFROA0802Domain.getGroupId() = " + requestSFROA0802Domain.getGroupId());
		log.debug("RequestSFROA0802Domain.getRequestSeq() = " + requestSFROA0802Domain.getRequestSeq());
		log.debug("RequestSFROA0802Domain.getOrderCode() = " + requestSFROA0802Domain.getOrderCode());
		requestSFROA0802Repository.save(requestSFROA0802Domain);
		
		// 조회
		Response response = openAPIRequestService.request(subUrl, request);
		log.debug("ResultCode = "	+ response.getHeader().getResultCode());
		log.debug("ResultMsg = " 	+ response.getHeader().getResultMsg());
		log.debug("NumOfRows = " 	+ response.getBody().getNumOfRows());
		log.debug("PageNo = " 		+ response.getBody().getPageNo());
		log.debug("TotalCount = " 	+ response.getBody().getTotalCount());
		
		// 조회조건 결과 저장
		requestSFROA0802Domain.setResultCode(response.getHeader().getResultCode());
		requestSFROA0802Domain.setResultMsg(response.getHeader().getResultMsg());
		requestSFROA0802Domain.setTotalCount(response.getBody().getTotalCount());
		requestSFROA0802Domain.setHashCode(FileUtil.getHashSHA256FromFilepath(agentRootDirectory+request.getGroupId()+"_"+request.getRequestSeq()+".xml"));	// Response 객채를 Hash로 변환해서 저장한다.
		requestSFROA0802Repository.saveAndFlush(requestSFROA0802Domain);
		
		if(response.getBody().getTotalCount() > 0 
				&& null != response.getBody().getItems()
				&& response.getBody().getItems().getItem().size() > 0){
			
			boolean bolWork = true;
			if(request.getRequestSeq() == 1){
				RequestSFROA0802Domain existRequestSFROA0802Domain = new RequestSFROA0802Domain();
				List<RequestSFROA0802Domain> listRequestSFROA0802Domain = requestSFROA0802Repository.findBySDateAndEDateAndOrderCode(request.getGroupId(), request.getSDate(), request.getEDate(), request.getOrderCode());
				if(null != listRequestSFROA0802Domain && listRequestSFROA0802Domain.size() > 0){
					existRequestSFROA0802Domain = listRequestSFROA0802Domain.get(0);
					if(null == existRequestSFROA0802Domain.getHashCode()){
						existRequestSFROA0802Domain.setHashCode("");
					}
					
					log.debug("#########################################");
					log.debug("["+existRequestSFROA0802Domain.getHashCode()+"]equals["+requestSFROA0802Domain.getHashCode()+"]");
					// Hash값이 같다면(똑같은 내용이라면 여러번 작업할 이유가 없으므로)
					if(existRequestSFROA0802Domain.getHashCode().equals(requestSFROA0802Domain.getHashCode())){
						bolWork = false;
					}
					log.debug(Boolean.toString(bolWork));
					log.debug("#########################################");
				}
			}
			
			if(bolWork){
				int seq = 1;
				for(Iterator<ResponseSFROA0802> iter = response.getBody().getItems().getItem().iterator(); iter.hasNext();){
					
					ResponseSFROA0802 item = iter.next();
					if(item.getOrderOrgNm().indexOf("한국공항공사") > -1){
						item.setGroupId(request.getGroupId());
						item.setRequestSeq(request.getRequestSeq());
						item.setSeq(seq++);
						listResponse.add(item);
					}
				}
			}
		}
		
		// 페이징
		Paging paging = new Paging();
		paging.setPageSize(response.getBody().getNumOfRows());
		paging.setPageNo(response.getBody().getPageNo());
		paging.setTotalCount(response.getBody().getTotalCount());
		
		// 다음 페이지 내용 가져오기
		if(paging.getNextPageNo() > response.getBody().getPageNo()){
			request.setPageNo(paging.getNextPageNo());
			request.setRequestSeq(paging.getNextPageNo());
			
			OpenAPIRequest openAPIRequest = OpenAPIContext.get();
			openAPIRequest.setRequestSeq(paging.getNextPageNo());
			OpenAPIContext.set(openAPIRequest);
			
			return getDataFromOpenAPI(subUrl, request, listResponse);
		}
		
		return response;
	}
	
	@Bean
	public Job job() throws Exception {
		return jobBuilderFactory.get("responseSFROA0802ExportToExternal")
	                            .start(step1())
	                            .listener(responseSFROA0802JobExecutionListener)
	                            .build();
	}
	
	@Bean
	public Step step1() throws Exception {
		return ((SimpleStepBuilder<ResponseSFROA0802Domain, ExternalResponseSFROA0802Domain>) stepBuilderFactory.get("responseSFROA0802DomainTransfer")
                .<ResponseSFROA0802Domain, ExternalResponseSFROA0802Domain> chunk(100) // 읽기/쓰기 단위
                .transactionManager(datasourceTwoTransactionManager))
                .reader(responseSFROA0802ItemReader)
                .writer(responseSFROA0802Writer())
                .processor(responseSFROA0802Processor())
                .listener(responseSFROA0802ItemWriterListener)
//                .taskExecutor(responseExecutor)
//                .throttleLimit(2) // 동시실행 쓰레드 갯수
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
	
	public <T> T getBeanInstance(Class<T> clazz){
		Object resultObject = BeanUtils.instantiate(clazz);
		
		Field[] fields = clazz.getDeclaredFields();
		
	    for (Field field : fields) {
	        ReflectionUtils.makeAccessible(field);
	        
	        if("orderCode".equals(field.getName())){
	        	ReflectionUtils.setField(field, resultObject, "test");
	        }
	    }
		
		return (T)resultObject;
	}
	
}
