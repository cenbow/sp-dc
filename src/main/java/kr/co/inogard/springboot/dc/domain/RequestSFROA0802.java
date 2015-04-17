package kr.co.inogard.springboot.dc.domain;

import lombok.Data;

@Data
public class RequestSFROA0802 extends Request {
	
	/**
	 * 발주기관(예 : 서울시)
	 */
	private String orderCode;
	
	/**
	 * 수요기관명(예 : 전라남도)
	 */
	private String demandCode;
	
	/**
	 * 공고/개찰일 유형(미입력 : 공고일1 입력 : 개찰일)
	 */
	private String dateType;
	
}
