package kr.co.inogard.springboot.dc;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import kr.co.inogard.springboot.dc.domain.Item;
import kr.co.inogard.springboot.dc.domain.Response;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println("Spring Boot Started.");
        
        RestTemplate restTemplate = new RestTemplate();
//        Page page = restTemplate.getForObject("http://graph.facebook.com/pivotalsoftware", Page.class);
//        System.out.println("Name:    " + page.getName());
//        System.out.println("About:   " + page.getAbout());
//        System.out.println("Phone:   " + page.getPhone());
//        System.out.println("Website: " + page.getWebsite());
        
        String serviceKey = "a123456A";
        serviceKey = "%2Bbsi1l6gma46SEQTP7kk%2FXk86tzaRV5DKkoVm3V22HLw4M2P028O1zBzsNInT2okg8YgbHePAxDE%2BY3gtiY65w%3D%3D";
        
        String url = "http://localhost:8080/getInsttAcctoThngListInfoFrgcpt";
        url = "http://openapi.g2b.go.kr/openapi/service/rest/BidPublicInfoService/getBidPblancListInfoThng";
//        url = "http://localhost:8888/openapi/service/rest/BidPublicInfoService/getBidPblancListInfoThng"; // TCP Mon
        
        Response response = null;
		try {
			response = restTemplate.getForObject(new URI(url+"?ServiceKey="+serviceKey), Response.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
        
        System.out.println("ResultCode = "	+ response.getHeader().getResultCode());
        System.out.println("ResultMsg = " 	+ response.getHeader().getResultMsg());
        System.out.println("NumOfRows = " 	+ response.getBody().getNumOfRows());
        System.out.println("PageNo = " 		+ response.getBody().getPageNo());
        System.out.println("TotalCount = " 	+ response.getBody().getTotalCount());
        System.out.println("getItems = " 	+ response.getBody().getItems());
        if(null != response.getBody().getItems()){
        	for(Item item : response.getBody().getItems().getItem()){
        		System.out.println("SupplyDate:	" + item.getSupplyDate());
        		System.out.println("eachLicenseAllowTypeOfBusiness:	" + item.getEachLicenseAllowTypeOfBusiness());
        	}
        }
    }

}