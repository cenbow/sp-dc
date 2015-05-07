package kr.co.inogard.springboot.dc.service;

import java.io.File;
import java.net.URI;

import kr.co.inogard.springboot.dc.domain.ResponseFileDomain;
import kr.co.inogard.springboot.dc.external.domain.ExternalResponseFileDomain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

@Component
public class ResponseFileItemProcessor implements ItemProcessor<ResponseFileDomain, ExternalResponseFileDomain> {
	
	private static final Logger log = LoggerFactory.getLogger(ResponseFileItemProcessor.class);
	
	@Value("${agent.fileroot}")
	private String agentFileRootDirectory;
	
	@Autowired
	private JpaRepository responseFileRepository;

    @Override
    public ExternalResponseFileDomain process(final ResponseFileDomain responseFileDomain) throws Exception {
    	
    	log.debug("첨부파일 다운로드 "+responseFileDomain.getUrl());
    	
    	Assert.notNull(responseFileDomain.getUrl(), "'url' must not be null");
    	
        RestTemplate restTemplate = new RestTemplate();
        
        HttpHeaders httpHeaders = restTemplate.headForHeaders(new URI(responseFileDomain.getUrl()));
        
        String contentDisposition = httpHeaders.getFirst(httpHeaders.CONTENT_DISPOSITION);
        log.debug("RAW "+httpHeaders.CONTENT_DISPOSITION+" "+contentDisposition);
        
        String keyWord = "filename=\"";
        int idx = contentDisposition.indexOf(keyWord);
        String fileName = contentDisposition.substring(idx+keyWord.length(), contentDisposition.indexOf("\"", idx+keyWord.length()));
        fileName = new String(fileName.getBytes("8859_1"), "KSC5601");

        log.debug("fileName ["+fileName+"]");
        
        responseFileDomain.setFileName(fileName);
        
        byte [] b = restTemplate.getForObject(new URI(responseFileDomain.getUrl()), byte[].class);
        
        File file = new File(agentFileRootDirectory+"/"+fileName);

        org.apache.commons.io.FileUtils.writeByteArrayToFile(file, b);
        
        log.debug(fileName + " 저장완료");
        
        responseFileDomain.setFilePath(file.getAbsolutePath());
        
        log.debug("ResponseFileDomain DB 저장 "+responseFileDomain);
        responseFileRepository.saveAndFlush(responseFileDomain);
        
        // TODO : 파일 복사 로직도 포함할지, Step으로 분리할지 고민
            
    	ExternalResponseFileDomain externalResponseFileDomain = new ExternalResponseFileDomain();
    	BeanUtils.copyProperties(responseFileDomain, externalResponseFileDomain);
        return externalResponseFileDomain;
    }

}