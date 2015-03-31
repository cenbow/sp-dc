package kr.co.inogard.springbiit.dc.controller;

import java.net.URI;
import java.net.URISyntaxException;

import junit.framework.Assert;
import kr.co.inogard.springboot.dc.controller.HelloController;
import kr.co.inogard.springboot.dc.domain.Item;
import kr.co.inogard.springboot.dc.domain.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MockServletContext.class)
@WebAppConfiguration
public class DataGovControllerTest {

	private MockMvc mvc;

	@Before
	public void setUp() throws Exception {
		mvc = MockMvcBuilders.standaloneSetup(new HelloController()).build();
	}

	@Test
	public void getData() throws Exception {

		RestTemplate restTemplate = new RestTemplate();

		String serviceKey = "%2Bbsi1l6gma46SEQTP7kk%2FXk86tzaRV5DKkoVm3V22HLw4M2P028O1zBzsNInT2okg8YgbHePAxDE%2BY3gtiY65w%3D%3D";

		String url = "http://openapi.g2b.go.kr/openapi/service/rest/BidPublicInfoService/getBidPblancListInfoThng";

		Response response = null;
		try {
			response = restTemplate.getForObject(new URI(url+"?ServiceKey="+serviceKey), Response.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		Assert.assertEquals("00", response.getHeader().getResultCode());
		
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
