package kr.co.inogard.springboot.dc.service;

import java.io.File;
import java.net.URI;
import java.util.concurrent.Future;

import kr.co.inogard.springboot.dc.domain.ResponseFileDomain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

@Service
public class AnnStdDocAsyncDownloadService {
	
	private static final Logger log = LoggerFactory.getLogger(OpenAPIRequestService.class);
	
	@Value("${agent.fileroot}")
	private String agentFileRootDirectory;
	
	@Autowired
	private JpaRepository responseFileRepository;

    @Async
    public Future<ResponseFileDomain> download(ResponseFileDomain responseFileDomain) throws Exception {
    	log.debug("÷������ �ٿ�ε� "+responseFileDomain.getUrl());
    	
    	Assert.notNull(responseFileDomain.getUrl(), "'url' must not be null");
    	
        RestTemplate restTemplate = new RestTemplate();
        
        HttpHeaders httpHeaders = restTemplate.headForHeaders(new URI(responseFileDomain.getUrl()));
        
        String contentDisposition = httpHeaders.getFirst(httpHeaders.CONTENT_DISPOSITION);
//        log.debug("RAW "+httpHeaders.CONTENT_DISPOSITION+" "+contentDisposition);
        
        String keyWord = "filename=\"";
        int idx = contentDisposition.indexOf(keyWord);
        String fileName = contentDisposition.substring(idx+keyWord.length(), contentDisposition.indexOf("\"", idx+keyWord.length()));
        fileName = new String(fileName.getBytes("8859_1"), "KSC5601");

        log.debug("fileName ["+fileName+"]");
        
        responseFileDomain.setFileName(fileName);
        
        byte [] b = restTemplate.getForObject(new URI(responseFileDomain.getUrl()), byte[].class);
        
        File file = new File(agentFileRootDirectory+"/"+fileName);

        org.apache.commons.io.FileUtils.writeByteArrayToFile(file, b);
        
        log.debug(fileName + " ����Ϸ�");
        
        responseFileDomain.setFilePath(file.getAbsolutePath());
        
        System.out.println("ResponseFileDomain DB ����"+responseFileDomain);
        responseFileRepository.saveAndFlush(responseFileDomain);
        
        return new AsyncResult<ResponseFileDomain>(responseFileDomain);
    }
	
}
