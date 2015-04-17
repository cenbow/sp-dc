package kr.co.inogard.springboot.dc.controller;

import java.util.ArrayList;
import java.util.List;

import kr.co.inogard.springboot.dc.domain.Body;
import kr.co.inogard.springboot.dc.domain.Header;
import kr.co.inogard.springboot.dc.domain.Items;
import kr.co.inogard.springboot.dc.domain.Response;
import kr.co.inogard.springboot.dc.domain.ResponseSFROA0802;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RESTServerController {

	private static final String SERVICE_KEY = "a123456A";
	
	@RequestMapping("/getInsttAcctoThngListInfoFrgcpt")
    public Response getInsttAcctoThngListInfoFrgcpt(@RequestParam(value="ServiceKey", required=false) String serviceKey) {

		boolean bolError = false;
		
		Response respose 	= new Response();
		Header header 		= new Header();
		Body body 			= new Body();
		
		if(null == serviceKey || "".equals(serviceKey)){
			bolError = true;
			header.setResultCode("99");
			header.setResultMsg("INVALID REQUEST PARAMETER ERROR.");
			
			body.setNumOfRows(0);
			body.setPageNo(0);
			body.setTotalCount(0);
		}
		
		if(!bolError && !SERVICE_KEY.equals(serviceKey)){
			bolError = true;
			header.setResultCode("99");
			header.setResultMsg("SERVICE KEY IS NOT REGISTERED ERROR.");
			
			body.setNumOfRows(0);
			body.setPageNo(0);
			body.setTotalCount(0);
		}
		
		if(!bolError){
			header.setResultCode("00");
			header.setResultMsg("NORMAL SERVICE.");
			
			Items items = new Items();

			List<ResponseSFROA0802> itemList = new ArrayList<>();
			
			ResponseSFROA0802 item = new ResponseSFROA0802();
			item.setSupplyDate("20150312");
			
			itemList.add(item);
			
			item = new ResponseSFROA0802();
			item.setSupplyDate("20150313");
			
			itemList.add(item);
			
			items.setItem(itemList);
			
			body.setNumOfRows(itemList.size());
			body.setPageNo(1);
			body.setTotalCount(1);
			body.setItems(items);
		}

		respose.setHeader(header);
		respose.setBody(body);
		return respose;
    }
	
}
