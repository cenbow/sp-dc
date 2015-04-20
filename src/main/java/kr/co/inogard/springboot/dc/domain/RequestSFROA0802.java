package kr.co.inogard.springboot.dc.domain;

import kr.co.inogard.springboot.dc.annotation.NeedURLEncodeAnnotation;
import lombok.Data;

@Data
public class RequestSFROA0802 extends Request {
	
	/**
	 * ���ֱ��(�� : �����)
	 */
	@NeedURLEncodeAnnotation
	private String orderCode;
	
	/**
	 * ��������(�� : ���󳲵�)
	 */
	@NeedURLEncodeAnnotation
	private String demandCode;
	
	/**
	 * ����/������ ����(���Է� : ������, 1 �Է� : ������)
	 */
	private String dateType;
	
}
