package kr.co.inogard.springboot.dc.domain;

import lombok.Data;

@Data
public class Request {
	
	private String groupId;
	
	/**
	 * �� ������ ��� ��
	 */
	private int numOfRows;
	
	/**
	 * ������ ��ȣ
	 */
	private int pageNo;
	
	/**
	 * ��ȸ��¥(����) YYYYMMDD
	 */
	private String sDate;
	
	/**
	 * ��ȸ��¥(��) YYYYMMDD
	 */
	private String eDate;
	
}
