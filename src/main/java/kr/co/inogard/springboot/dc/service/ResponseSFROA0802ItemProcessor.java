package kr.co.inogard.springboot.dc.service;

import kr.co.inogard.springboot.dc.domain.ResponseSFROA0802Domain;
import kr.co.inogard.springboot.dc.external.domain.ExternalResponseSFROA0802Domain;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.BeanUtils;

public class ResponseSFROA0802ItemProcessor implements ItemProcessor<ResponseSFROA0802Domain, ExternalResponseSFROA0802Domain> {

    @Override
    public ExternalResponseSFROA0802Domain process(final ResponseSFROA0802Domain responseSFROA0802Domain) throws Exception {
    	ExternalResponseSFROA0802Domain externalResponseSFROA0802Domain = new ExternalResponseSFROA0802Domain();
    	BeanUtils.copyProperties(responseSFROA0802Domain, externalResponseSFROA0802Domain);
        return externalResponseSFROA0802Domain;
    }

}