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
import kr.co.inogard.springboot.dc.domain.RequestSFROA0802;
import kr.co.inogard.springboot.dc.domain.RequestSFROA0802Domain;
import kr.co.inogard.springboot.dc.domain.Response;
import kr.co.inogard.springboot.dc.domain.ResponseFileDomain;
import kr.co.inogard.springboot.dc.domain.ResponseSFROA0802;
import kr.co.inogard.springboot.dc.domain.ResponseSFROA0802Domain;
import kr.co.inogard.springboot.dc.external.domain.ExternalResponseSFROA0802Domain;
import kr.co.inogard.springboot.dc.service.AnnStdDocAsyncDownloadService;
import kr.co.inogard.springboot.dc.service.OpenAPIContext;
import kr.co.inogard.springboot.dc.service.OpenAPIRequestService;
import kr.co.inogard.springboot.dc.service.Paging;
import kr.co.inogard.springboot.dc.service.ResponseSFROA0802ItemProcessor;
import kr.co.inogard.springboot.dc.service.ResponseSFROA0802ItemReader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.converter.DefaultJobParametersConverter;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
	
	@Autowired
	private JpaRepository requestSFROA0802Repository;
	
	@Autowired
	private JpaRepository responseSFROA0802Repository;
	
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
	private ResponseSFROA0802ItemReader ResponseSFROA0802ItemReader;

	@Test
	public void getData() throws Exception {
		
		// 업무 트렌젝션(DB아님) 단위의 키로 사용할 값을 생성하여 OpenAPIContext에 담아서 같은 값을 사용할 수 있도록 함.
		String timestamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		OpenAPIContext.set(timestamp);
		
//		RequestSFROA0802 requestSFROA0802 = this.getBeanInstance(RequestSFROA0802.class);
		
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
		
		String subUrl = "BidPublicInfoService/getInsttAcctoBidPblancListThng";
		
		Response response = this.getDataFromOpenAPI(subUrl, request, listResponse);
		System.out.println("ResultCode = "	+ response.getHeader().getResultCode());
		System.out.println("ResultMsg = " 	+ response.getHeader().getResultMsg());
		System.out.println("NumOfRows = " 	+ response.getBody().getNumOfRows());
		System.out.println("PageNo = " 		+ response.getBody().getPageNo());
		System.out.println("TotalCount = " 	+ response.getBody().getTotalCount());
		
		System.out.println("TotalResultCount : " + listResponse.size());
		
		List<ResponseFileDomain> listDownloadFileCandidate = new ArrayList<>();
		for(ResponseSFROA0802 responseSFROA0802 : listResponse){
			System.out.println(responseSFROA0802);
			
			ResponseSFROA0802Domain responseSFROA0802Domain = new ResponseSFROA0802Domain();
			BeanUtils.copyProperties(responseSFROA0802, responseSFROA0802Domain);
			
			if(null != responseSFROA0802.getAnnStdDoc1()
					&& !"".equals(responseSFROA0802.getAnnStdDoc1())){
				
				ResponseFileDomain responseFileDomain = new ResponseFileDomain();
				responseFileDomain.setGroupId(responseSFROA0802Domain.getGroupId());
				responseFileDomain.setRequestSeq(responseSFROA0802Domain.getRequestSeq());
				responseFileDomain.setSeq(responseSFROA0802Domain.getSeq());
				responseFileDomain.setUrl(responseSFROA0802.getAnnStdDoc1());
				
				listDownloadFileCandidate.add(responseFileDomain);
			}			
			responseSFROA0802Repository.save(responseSFROA0802Domain);
		}
		responseSFROA0802Repository.flush();
		
		try {
			System.out.println("#############################");
			System.out.println("비동기 호츌 시작");
			System.out.println("#############################");
			
			for(ResponseFileDomain responseFileDomain : listDownloadFileCandidate){
				Future<ResponseFileDomain> future = annStdDocAsyncDownloadService.download(responseFileDomain);
				if(future.isDone()){
					System.out.println("######## 비동기 작업 완료 ########");
				}
			}
			System.out.println("#############################");
			System.out.println("비동기 호츌 끝");
			System.out.println("#############################");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("#############################");
		System.out.println("업무 끝");
		System.out.println("#############################");	
		
		
		runBatch(timestamp);
		
		Thread.sleep(15000);
		
		// 사용이 끝나면 삭제하기
		OpenAPIContext.reset();
	}
	
	public Response getDataFromOpenAPI(String subUrl, RequestSFROA0802 request, List<ResponseSFROA0802> listResponse) throws Exception{
		
		RequestSFROA0802Domain requestSFROA0802Domain = new RequestSFROA0802Domain();
		BeanUtils.copyProperties(request, requestSFROA0802Domain);
		
		System.out.println("RequestSFROA0802Domain.getGroupId() = " + requestSFROA0802Domain.getGroupId());
		System.out.println("RequestSFROA0802Domain.getRequestSeq() = " + requestSFROA0802Domain.getRequestSeq());
		System.out.println("RequestSFROA0802Domain.getOrderCode() = " + requestSFROA0802Domain.getOrderCode());
		
		requestSFROA0802Repository.saveAndFlush(requestSFROA0802Domain);
		
		Response response = openAPIRequestService.request(subUrl, request);
		System.out.println("ResultCode = "	+ response.getHeader().getResultCode());
		System.out.println("ResultMsg = " 	+ response.getHeader().getResultMsg());
		System.out.println("NumOfRows = " 	+ response.getBody().getNumOfRows());
		System.out.println("PageNo = " 		+ response.getBody().getPageNo());
		System.out.println("TotalCount = " 	+ response.getBody().getTotalCount());
		
		if(response.getBody().getTotalCount() > 0 
				&& null != response.getBody().getItems()
				&& response.getBody().getItems().getItem().size() > 0){
			
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
		
		// 페이징
		Paging paging = new Paging();
		paging.setPageSize(response.getBody().getNumOfRows());
		paging.setPageNo(response.getBody().getPageNo());
		paging.setTotalCount(response.getBody().getTotalCount());
		
		// 다음 페이지 내용 가져오기
		if(paging.getNextPageNo() > response.getBody().getPageNo()){
			request.setPageNo(paging.getNextPageNo());
			request.setRequestSeq(paging.getNextPageNo());
			
			return getDataFromOpenAPI(subUrl, request, listResponse);
		}
		
		return response;
	}

	private void runBatch(String timestamp) throws Exception,
			JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		// Batch 시작
		Properties prop = new Properties();
		prop.setProperty("groupId", timestamp);
		System.out.println("####################################");
		System.out.println("####################################");
		System.out.println("groupId = " + timestamp);
		System.out.println("####################################");
		System.out.println("####################################");
		
		JobParametersConverter jobParametersConverter = new DefaultJobParametersConverter();
		JobParameters jobParameters = jobParametersConverter.getJobParameters(prop);
		
		JobRepositoryFactoryBean jobRepositoryFactoryBean = new JobRepositoryFactoryBean();
		jobRepositoryFactoryBean.setTransactionManager(datasourceOneTransactionManager);
		jobRepositoryFactoryBean.setDataSource(dataSource);
		
		SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor();
		
		SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();
        simpleJobLauncher.setJobRepository(jobRepositoryFactoryBean.getObject());
        simpleJobLauncher.setTaskExecutor(simpleAsyncTaskExecutor);
        simpleJobLauncher.afterPropertiesSet();
        JobExecution execution = simpleJobLauncher.run(job(), jobParameters);
        System.out.println("Exit Status : " + execution.getStatus());
	}
	
	@Bean
	public Job job() throws Exception {
		return jobBuilderFactory.get("responseSFROA0802ExportToExternal")
	                            .start(step1())
	                            .build();
	}
	
	@Bean
	public Step step1() throws Exception {
		return ((SimpleStepBuilder<ResponseSFROA0802Domain, ExternalResponseSFROA0802Domain>) stepBuilderFactory.get("response")
                .<ResponseSFROA0802Domain, ExternalResponseSFROA0802Domain> chunk(100) // 읽기/쓰기 단위
                .transactionManager(datasourceTwoTransactionManager))
                .reader(ResponseSFROA0802ItemReader)
                .writer(responseWriter())
                .processor(responseProcessor())
//                .taskExecutor(responseExecutor)
//                .throttleLimit(2) // 동시실행 쓰레드 갯수
                .build();
	}
	
	@Bean
	public JpaItemWriter<ExternalResponseSFROA0802Domain> responseWriter() {
    	JpaItemWriter writer = new JpaItemWriter();
		writer.setEntityManagerFactory(datasourceTwoEntityManager);
	    return writer;
	}
	
	@Bean
	public ItemProcessor<ResponseSFROA0802Domain, ExternalResponseSFROA0802Domain> responseProcessor() {
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
