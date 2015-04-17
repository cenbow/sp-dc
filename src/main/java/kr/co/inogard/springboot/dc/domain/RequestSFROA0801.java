package kr.co.inogard.springboot.dc.domain;

import lombok.Data;

@Data
public class RequestSFROA0801 extends Request {
	
	/**
	 * 발주기관(예 : 서울시)
	 */
	private String orderCode;
	
	/**
	 * 실수요기관
	 */
	private String realCode;
	
	/**
	 * 품목명
	 */
	private String goods;

}
