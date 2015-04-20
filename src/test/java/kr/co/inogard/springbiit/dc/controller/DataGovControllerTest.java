package kr.co.inogard.springbiit.dc.controller;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import kr.co.inogard.springboot.dc.Application;
import kr.co.inogard.springboot.dc.domain.RequestSFROA0802;
import kr.co.inogard.springboot.dc.domain.RequestSFROA0802Domain;
import kr.co.inogard.springboot.dc.domain.Response;
import kr.co.inogard.springboot.dc.domain.ResponseSFROA0802;
import kr.co.inogard.springboot.dc.service.OpenAPIContext;
import kr.co.inogard.springboot.dc.service.OpenAPIRequestService;
import kr.co.inogard.springboot.dc.service.Paging;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.ReflectionUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class DataGovControllerTest {
	
	@Autowired
	private JpaRepository requestSFROA0802Repository;
	
	@Autowired
	private OpenAPIRequestService openAPIRequestService;

	@Test
	public void getData() throws Exception {
		
		String timestamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		OpenAPIContext.set(timestamp);
		
		List<ResponseSFROA0802> listResponse = new ArrayList();
		
		RequestSFROA0802 requestSFROA0802 = this.getBeanInstance(RequestSFROA0802.class);
		
		String subUrl = "BidPublicInfoService/getInsttAcctoBidPblancListThng";
		
		int pageSize 	= 200;
		int pageNo 		= 1;
		
		RequestSFROA0802 request = new RequestSFROA0802();
		request.setGroupId("test");
		request.setNumOfRows(pageSize);
		request.setPageNo(pageNo);
		request.setSDate("20150401");
		request.setEDate("20150420");
		request.setOrderCode("한국공항공사");
		
		Response response = this.getDataFromOpenAPI(subUrl, request, listResponse);
		System.out.println("ResultCode = "	+ response.getHeader().getResultCode());
		System.out.println("ResultMsg = " 	+ response.getHeader().getResultMsg());
		System.out.println("NumOfRows = " 	+ response.getBody().getNumOfRows());
		System.out.println("PageNo = " 		+ response.getBody().getPageNo());
		System.out.println("TotalCount = " 	+ response.getBody().getTotalCount());
		
		System.out.println("TotalResultCount : " + listResponse.size());
		for(ResponseSFROA0802 responseSFROA0802 : listResponse){
			System.out.println(responseSFROA0802);
		}
		
		OpenAPIContext.reset();
	}
	
	public Response getDataFromOpenAPI(String subUrl, RequestSFROA0802 request, List<ResponseSFROA0802> listResponse) throws Exception{
		
		RequestSFROA0802Domain requestSFROA0802Domain = new RequestSFROA0802Domain();
		BeanUtils.copyProperties(request, requestSFROA0802Domain);
		
		System.out.println("RequestSFROA0802Domain.getGroupId() = " + requestSFROA0802Domain.getGroupId());
		System.out.println("RequestSFROA0802Domain.getRequestSeq() = " + requestSFROA0802Domain.getRequestSeq());
		System.out.println("RequestSFROA0802Domain.getOrderCode() = " + requestSFROA0802Domain.getOrderCode());
		
		requestSFROA0802Repository.saveAndFlush(requestSFROA0802Domain);
		List<RequestSFROA0802Domain> list = requestSFROA0802Repository.findAll();
		for(RequestSFROA0802Domain requestSFROA0802Domain2 : list){
			System.out.println("requestSFROA0802Domain2.getGroupId() = " + requestSFROA0802Domain2.getGroupId());
			System.out.println("requestSFROA0802Domain2.getRequestSeq() = " + requestSFROA0802Domain2.getRequestSeq());
			System.out.println("requestSFROA0802Domain2.getOrderCode() = " + requestSFROA0802Domain2.getOrderCode());
		}
		
		Response response = openAPIRequestService.request(subUrl, request);
		System.out.println("ResultCode = "	+ response.getHeader().getResultCode());
		System.out.println("ResultMsg = " 	+ response.getHeader().getResultMsg());
		System.out.println("NumOfRows = " 	+ response.getBody().getNumOfRows());
		System.out.println("PageNo = " 		+ response.getBody().getPageNo());
		System.out.println("TotalCount = " 	+ response.getBody().getTotalCount());
		
		if(response.getBody().getTotalCount() > 0 
				&& null != response.getBody().getItems()
				&& response.getBody().getItems().getItem().size() > 0){
			
			for(Iterator<ResponseSFROA0802> iter = response.getBody().getItems().getItem().iterator(); iter.hasNext();){
				
				ResponseSFROA0802 item = iter.next();
				if(item.getOrderOrgNm().indexOf("한국공항공사") > -1){
					System.out.println("입찰공고번호|발주기관코드|발주기관:	[" + item.getBidNo()+"] ["+item.getOrderOrgCode()+"] ["+item.getOrderOrgNm()+"]");
					listResponse.add(item);
				}
			}
		}
		
		// 페이징
		Paging paging = new Paging();
		paging.setPageSize(response.getBody().getNumOfRows());
		paging.setPageNo(response.getBody().getPageNo());
		paging.setTotalCount(response.getBody().getTotalCount());
		
		// 다음 페이지 내용 가져오기
		if(paging.getNextPageNo() > response.getBody().getPageNo()){
			request.setPageNo(paging.getNextPageNo());
			
			return getDataFromOpenAPI(subUrl, request, listResponse);
		}
		
		return response;
	}
	
	public <T> T getBeanInstance(Class<T> clazz){
		Object resultObject = BeanUtils.instantiate(clazz);
		
		Field[] fields = clazz.getDeclaredFields();
		
	    for (Field field : fields) {
	        ReflectionUtils.makeAccessible(field);
	        
	        if("orderCode".equals(field.getName())){
	        	ReflectionUtils.setField(field, resultObject, "test");
	        }
	    }
		
		return (T)resultObject;
	}
	
}
