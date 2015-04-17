package kr.co.inogard.springboot.dc.domain;

import lombok.Data;

@Data
public class RequestSFROA0802 extends Request {
	
	/**
	 * ���ֱ��(�� : �����)
	 */
	private String orderCode;
	
	/**
	 * ��������(�� : ���󳲵�)
	 */
	private String demandCode;
	
	/**
	 * ����/������ ����(���Է� : ������1 �Է� : ������)
	 */
	private String dateType;
	
}
