package kr.co.inogard.springboot.dc.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import kr.co.inogard.springboot.dc.domain.RequestSFROA0802;
import kr.co.inogard.springboot.dc.domain.RequestSFROA0802Domain;
import kr.co.inogard.springboot.dc.domain.RequestSFROA0802DomainKey;
import kr.co.inogard.springboot.dc.domain.Response;
import kr.co.inogard.springboot.dc.domain.ResponseFileDomain;
import kr.co.inogard.springboot.dc.domain.ResponseSFROA0802;
import kr.co.inogard.springboot.dc.domain.ResponseSFROA0802Domain;
import kr.co.inogard.springboot.dc.repository.RequestSFROA0802Repository;
import kr.co.inogard.springboot.dc.utils.FileUtil;
import kr.co.inogard.springboot.dc.utils.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public class RequestSFROA802Tasklet implements Tasklet {
	
	private static final Logger log = LoggerFactory.getLogger(RequestSFROA802Tasklet.class);
	
	@Value("${agent.root}")
	private String agentRootDirectory;
	
	@Autowired
	private OpenAPIRequestService openAPIRequestService;
	
	@Autowired
	private RequestSFROA0802Repository requestSFROA0802Repository;
	
	@Autowired
	private JpaRepository responseSFROA0802Repository;
	
	@Autowired
	private JpaRepository responseFileRepository;

	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		
		JobParameters jobParameters = chunkContext.getStepContext().getStepExecution().getJobParameters();

		String groupId 		= new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		String sDate 		= StringUtil.nvl(jobParameters.getString("sDate"));
		String eDate		= StringUtil.nvl(jobParameters.getString("eDate"));
		String orderCode	= StringUtil.nvl(jobParameters.getString("orderCode"));
		
		int requestSeq 	= 1;
		int pageSize 	= 200;
		int pageNo 		= 1;
		
		String subUrl = "BidPublicInfoService/getInsttAcctoBidPblancListThng";
		
		RequestSFROA0802 request = new RequestSFROA0802();
		request.setGroupId(groupId);
		request.setRequestSeq(requestSeq);
		request.setNumOfRows(pageSize);
		request.setPageNo(pageNo);
		request.setSDate(sDate);
		request.setEDate(eDate);
		request.setOrderCode(orderCode);
		
		// JobExecutionId를 포함한 조회조건 저장
		RequestSFROA0802Domain requestSFROA0802Domain = new RequestSFROA0802Domain();
		BeanUtils.copyProperties(request, requestSFROA0802Domain);
		
		log.debug("JobExecutionId : "+chunkContext.getStepContext().getStepExecution().getJobExecutionId());
		log.debug("Status : "+chunkContext.getStepContext().getStepExecution().getStatus().toString());
		
		requestSFROA0802Domain.setJobExecutionId(chunkContext.getStepContext().getStepExecution().getJobExecutionId());
        requestSFROA0802Domain.setJobExecutionStatus(chunkContext.getStepContext().getStepExecution().getStatus().toString());
		requestSFROA0802Repository.save(requestSFROA0802Domain);
		
		List<ResponseSFROA0802> listResponse = new ArrayList();
		
		Response response = this.getDataFromOpenAPI(subUrl, request, listResponse);
		
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
		
		return RepeatStatus.FINISHED;
	}
	
	public Response getDataFromOpenAPI(String subUrl, RequestSFROA0802 request, List<ResponseSFROA0802> listResponse) throws Exception{
		
		if(request.getRequestSeq() > 1){
			RequestSFROA0802Domain requestSFROA0802Domain = new RequestSFROA0802Domain();
			BeanUtils.copyProperties(request, requestSFROA0802Domain);
			
			// 조회조건 저장
			log.debug("RequestSFROA0802Domain.getGroupId() = " + requestSFROA0802Domain.getGroupId());
			log.debug("RequestSFROA0802Domain.getRequestSeq() = " + requestSFROA0802Domain.getRequestSeq());
			log.debug("RequestSFROA0802Domain.getOrderCode() = " + requestSFROA0802Domain.getOrderCode());
			requestSFROA0802Repository.save(requestSFROA0802Domain);
		}

		RequestSFROA0802DomainKey id = new RequestSFROA0802DomainKey();
		id.setGroupId(request.getGroupId());
		id.setRequestSeq(request.getRequestSeq());
		RequestSFROA0802Domain requestSFROA0802Domain = requestSFROA0802Repository.findOne(id);

		// 조회
		Response response = openAPIRequestService.request(subUrl, request);
		log.debug("ResultCode = "	+ response.getHeader().getResultCode());
		log.debug("ResultMsg = " 	+ response.getHeader().getResultMsg());
		log.debug("NumOfRows = " 	+ response.getBody().getNumOfRows());
		log.debug("PageNo = " 		+ response.getBody().getPageNo());
		log.debug("TotalCount = " 	+ response.getBody().getTotalCount());
		
		String saveFileName = RestTemplateSaveFileHolder.get();
		RestTemplateSaveFileHolder.reset();
		
		// 조회조건 결과 저장
		requestSFROA0802Domain.setResultCode(response.getHeader().getResultCode());
		requestSFROA0802Domain.setResultMsg(response.getHeader().getResultMsg());
		requestSFROA0802Domain.setTotalCount(response.getBody().getTotalCount());
		requestSFROA0802Domain.setHashCode(FileUtil.getHashSHA256FromFilepath(agentRootDirectory+saveFileName+".xml"));	// Response 객채를 Hash로 변환해서 저장한다.
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
			
			return getDataFromOpenAPI(subUrl, request, listResponse);
		}
		
		return response;
	}

}
