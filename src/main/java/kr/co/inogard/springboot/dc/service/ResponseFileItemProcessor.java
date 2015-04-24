package kr.co.inogard.springboot.dc.service;

import kr.co.inogard.springboot.dc.domain.ResponseFileDomain;
import kr.co.inogard.springboot.dc.external.domain.ExternalResponseFileDomain;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.BeanUtils;

public class ResponseFileItemProcessor implements ItemProcessor<ResponseFileDomain, ExternalResponseFileDomain> {

    @Override
    public ExternalResponseFileDomain process(final ResponseFileDomain responseFileDomain) throws Exception {
    	ExternalResponseFileDomain externalResponseFileDomain = new ExternalResponseFileDomain();
    	BeanUtils.copyProperties(responseFileDomain, externalResponseFileDomain);
        return externalResponseFileDomain;
    }

}