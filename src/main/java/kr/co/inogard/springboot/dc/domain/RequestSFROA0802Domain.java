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
	 * 한 페이지 결과 수
	 */
	@Column(name="numOfRows")
	private int numOfRows;
	
	/**
	 * 페이지 번호
	 */
	@Column(name="pageNo")
	private int pageNo;
	
	/**
	 * 조회날짜(시작) YYYYMMDD
	 */
	@Column(name="sDate", length=8)
	private String sDate;
	
	/**
	 * 조회날짜(끝) YYYYMMDD
	 */
	@Column(name="eDate", length=8)
	private String eDate;
	
	/**
	 * 발주기관(예 : 서울시)
	 */
	@Column(name="orderCode", length=100)
	private String orderCode;
	
	/**
	 * 수요기관명(예 : 전라남도)
	 */
	@Column(name="demandCode", length=100)
	private String demandCode;
	
	/**
	 * 공고/개찰일 유형(미입력 : 공고일, 1 입력 : 개찰일)
	 */
	@Column(name="dateType", length=1)
	private String dateType;
	
}
