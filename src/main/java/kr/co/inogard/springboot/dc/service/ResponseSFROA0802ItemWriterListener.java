package kr.co.inogard.springboot.dc.service;

import java.util.List;

import kr.co.inogard.springboot.dc.domain.ResponseSFROA0802Domain;
import kr.co.inogard.springboot.dc.external.domain.ExternalResponseSFROA0802Domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public class ResponseSFROA0802ItemWriterListener implements ItemWriteListener<ExternalResponseSFROA0802Domain> {
	
	private static final Logger log = LoggerFactory.getLogger(ResponseSFROA0802ItemWriterListener.class);

	@Autowired
	private JpaRepository responseSFROA0802Repository;
	
	@Override
	public void beforeWrite(List<? extends ExternalResponseSFROA0802Domain> items) {
		
	}

	@Override
	public void afterWrite(List<? extends ExternalResponseSFROA0802Domain> items) {
		log.debug("################################################");
		log.debug("afterWrite start");
		boolean isUpdate = false;
		for(ExternalResponseSFROA0802Domain externalResponseSFROA0802Domain : items){
			Object o = responseSFROA0802Repository.findOne(externalResponseSFROA0802Domain.getBidNo());
			if(null != o){
				ResponseSFROA0802Domain responseSFROA0802Domain = (ResponseSFROA0802Domain)o;
				responseSFROA0802Domain.setTransferYn("Y");
				responseSFROA0802Repository.save(responseSFROA0802Domain);
				isUpdate = true;
			}
		}
		if(isUpdate){
			responseSFROA0802Repository.flush();
		}
		log.debug("afterWrite end");
		log.debug("################################################");
	}

	@Override
	public void onWriteError(Exception exception, List<? extends ExternalResponseSFROA0802Domain> items) {
		log.error("ResponseSFROA0802ItemWriterListener.onWriteError", exception);	
	}
	
}
