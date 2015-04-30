package kr.co.inogard.springboot.dc.service;

import kr.co.inogard.springboot.dc.domain.RequestSFROA0802Domain;
import kr.co.inogard.springboot.dc.repository.RequestSFROA0802Repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResponseSFROA0802JobExecutionListener implements JobExecutionListener {
	
	private static final Logger log = LoggerFactory.getLogger(ResponseSFROA0802JobExecutionListener.class);
	
	@Autowired
	private RequestSFROA0802Repository requestSFROA0802Repository;

	@Override
	public void beforeJob(JobExecution jobExecution) {

	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		log.debug("################################################");
		log.debug("afterJob start");
		RequestSFROA0802Domain requestSFROA0802Domain = requestSFROA0802Repository.findByJobExcutionId(jobExecution.getId());
		if(null != requestSFROA0802Domain){
			requestSFROA0802Domain.setJobExecutionStatus(jobExecution.getExitStatus().getExitCode());
			requestSFROA0802Repository.saveAndFlush(requestSFROA0802Domain);
		}
		log.debug("afterJob end");
		log.debug("################################################");
	}

}