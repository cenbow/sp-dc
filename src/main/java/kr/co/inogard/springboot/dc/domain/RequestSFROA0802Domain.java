package kr.co.inogard.springboot.dc.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@IdClass(RequestSFROA0802DomainKey.class)
@Table(name="RequestSFROA0802")
public class RequestSFROA0802Domain {

	@Id
	@Column(name="groupId")
	private String groupId;
	
	@Id
//	@GeneratedValue(strategy=GenerationType.AUTO)
//	@GeneratedValue(strategy=GenerationType.IDENTITY)
//	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	@Column(name="requestSeq")
	private Integer requestSeq;
	
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
