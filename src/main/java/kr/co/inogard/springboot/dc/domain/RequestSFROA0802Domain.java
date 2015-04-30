package kr.co.inogard.springboot.dc.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;

@Entity(name="RequestSFROA0802")
@Data
@IdClass(RequestSFROA0802DomainKey.class)
@Table(name="RequestSFROA0802")
public class RequestSFROA0802Domain {

	@Id
	@Column(name="groupId")
	private String groupId;
	
	@Id
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
	 * ����ڵ�
	 */
	@Column(name="resultCode", length=2)
	private String resultCode;
	
	/**
	 * ����޼���
	 */
	@Column(name="resultMsg", length=50)
	private String resultMsg;
	
	/**
	 * ��ü ��� ��
	 */
	@Column(name="totalCount")
	private int totalCount;
	
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
	
	/**
	 * Hash ��
	 */
	@Column(name="hashCode", length=100)
	private String hashCode;
	
	/**
	 * Batch�� Jab ���� ID
	 */
	@Column(name="jobExecutionId")
	private long jobExecutionId;
	
	/**
	 * Batch�� Jab ���� ID
	 */
	@Column(name="jobExecutionStatus", length=10)
	private String jobExecutionStatus;
	
}
