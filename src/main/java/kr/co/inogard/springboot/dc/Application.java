package kr.co.inogard.springboot.dc;

import java.util.HashMap;
import java.util.Map;

import kr.co.inogard.springboot.dc.domain.Item;
import kr.co.inogard.springboot.dc.domain.Response;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
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
        
        restTemplate = new RestTemplate();
        Map<String, String> map = new HashMap<>();
        map.put("ServiceKey", "a123456A");
        Response response = restTemplate.getForObject("http://localhost:8080/getInsttAcctoThngListInfoFrgcpt?ServiceKey=a123456A", Response.class, map);
        System.out.println("ResultCode:	" + response.getHeader().getResultCode());
        System.out.println("ResultMsg:	" + response.getHeader().getResultMsg());
        if(null != response.getBody().getItems()){
        	for(Item item : response.getBody().getItems()){
        		System.out.println("SupplyDate:	" + item.getSupplyDate());		
        	}
        }
    }

}