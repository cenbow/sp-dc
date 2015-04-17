package kr.co.inogard.springboot.dc.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="RequestSFROA0802")
public class RequestSFROA0802Domain {

	private String groupId;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int requestSeq;
	
	/**
	 * �� ������ ��� ��
	 */
	@Column(name="numOfRows")
	private int numOfRows;
	
	/**
	 * ������ ��ȣ
	 */
	@Column(name="pageNo")
	private int pageNo;
	
	/**
	 * ��ȸ��¥(����) YYYYMMDD
	 */
	@Column(name="sDate", length=8)
	private String sDate;
	
	/**
	 * ��ȸ��¥(��) YYYYMMDD
	 */
	@Column(name="eDate", length=8)
	private String eDate;
	
	/**
	 * ���ֱ��(�� : �����)
	 */
	@Column(name="orderCode", length=100)
	private String orderCode;
	
	/**
	 * ��������(�� : ���󳲵�)
	 */
	@Column(name="demandCode", length=100)
	private String demandCode;
	
	/**
	 * ����/������ ����(���Է� : ������, 1 �Է� : ������)
	 */
	@Column(name="dateType", length=1)
	private String dateType;
	
}
