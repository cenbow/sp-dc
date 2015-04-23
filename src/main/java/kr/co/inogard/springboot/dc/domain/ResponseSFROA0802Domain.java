package kr.co.inogard.springboot.dc.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@IdClass(ResponseSFROA0802DomainKey.class)
@Table(name="ResponseSFROA0802")
public class ResponseSFROA0802Domain {
	
	@Id
	@Column(name="groupId")
	private String groupId;
	
	@Id
	@Column(name="requestSeq")
	private int requestSeq;
	
	@Id
//	@GeneratedValue(strategy=GenerationType.AUTO)
//	@GeneratedValue(strategy=GenerationType.IDENTITY)
//	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	@Column(name="seq")
	private Integer seq;
	
	@Column(name="bidNo", length=40)
	private String bidNo;
	
	@Column(name="supplyDate", length=5)
	private String supplyDate;
	
	@Column(name="echLicnAlowTypeBusi")
	private String eachLicenseAllowTypeOfBusiness;
	
	@Column(name="orderOrgCode", length=7)
	private String orderOrgCode;
	
	@Column(name="orderOrgNm", length=100)
	private String orderOrgNm;
	
	@Column(name="annStdDoc1", length=400)
	private String annStdDoc1;
	
}
