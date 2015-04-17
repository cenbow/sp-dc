package kr.co.inogard.springboot.dc.service;

import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;

import kr.co.inogard.springboot.dc.domain.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenAPIRequestService {
	
	private static final Logger log = LoggerFactory.getLogger(OpenAPIRequestService.class);

	@Value("${data_go_kr.url}")
	private String url;
	
	@Value("${data_go_kr.serviceKey}")
	private String serviceKey;
	
	public Response request(String subUrl, Object paramClass) throws Exception{
		
		Assert.notNull(url, "'url' must not be null");
		Assert.notNull(serviceKey, "'serviceKey' must not be null");
		Assert.notNull(paramClass, "'paramClass' must not be null");
		
		StringBuffer sbfullURI	 	= new StringBuffer();
		StringBuffer sbhttpGetParam = new StringBuffer();
		
		try {
			// Requst Class의 내용
			Field[] field = paramClass.getClass().getSuperclass().getDeclaredFields();
			for(Field f :field){
				ReflectionUtils.makeAccessible(f);
				String fieldName = f.getName();
				
				Object o = ReflectionTestUtils.invokeGetterMethod(paramClass, fieldName);
				if(null != o){
					sbhttpGetParam.append("&"+fieldName+"="+o.toString());
				}
			}
			
			// Requst Class를 상속받은 클래스의 내용
			field = paramClass.getClass().getDeclaredFields();
			for(Field f :field){
				ReflectionUtils.makeAccessible(f);
				String fieldName = f.getName();
				
				Object o = ReflectionTestUtils.invokeGetterMethod(paramClass, fieldName);
				if(null != o){
					sbhttpGetParam.append("&"+fieldName+"="+o.toString());
				}
			}
		} catch (Exception e) {
			log.error("URI Param Building Exception", e);
			throw e;
		}
		
		sbfullURI.append(url).append(subUrl).append("?ServiceKey=").append(serviceKey).append(sbhttpGetParam);
		
		log.info("Request URI = ["+sbfullURI+"]");
		
		Response response = null;
		RestTemplate restTemplate = new RestTemplate();
		try {
			response = restTemplate.getForObject(new URI(sbfullURI.toString()), Response.class);
		} catch (RestClientException e) {
			e.printStackTrace();
			log.error("RestClientException", e);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			log.error("URISyntaxException", e);
		}
		
		log.info("ResultCode = "	+ response.getHeader().getResultCode());
		log.info("ResultMsg = " 	+ response.getHeader().getResultMsg());
		
		return response;
	}
}
