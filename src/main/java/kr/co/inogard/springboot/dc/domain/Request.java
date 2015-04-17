package kr.co.inogard.springboot.dc.domain;

import lombok.Data;

@Data
public class Request {
	
	private String groupId;
	
	/**
	 * 한 페이지 결과 수
	 */
	private int numOfRows;
	
	/**
	 * 페이지 번호
	 */
	private int pageNo;
	
	/**
	 * 조회날짜(시작) YYYYMMDD
	 */
	private String sDate;
	
	/**
	 * 조회날짜(끝) YYYYMMDD
	 */
	private String eDate;
	
}
