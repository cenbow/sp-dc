package kr.co.inogard.springboot.dc.domain;

import lombok.Data;

@Data
public class RequestSFROA0801 extends Request {
	
	/**
	 * ���ֱ��(�� : �����)
	 */
	private String orderCode;
	
	/**
	 * �Ǽ�����
	 */
	private String realCode;
	
	/**
	 * ǰ���
	 */
	private String goods;

}
