package kr.co.inogard.springboot.dc.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="ResponseSFROA0802")
public class ResponseSFROA0802Domain implements Serializable {
	
	private static final long serialVersionUID = 1448570673770028678L;

	@Id
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
	
	@Column(name="transferYn", length=1)
	private String transferYn;
	
//	@OneToMany(fetch=FetchType.LAZY
//			, mappedBy="responseSFROA0802Domain"
//			, cascade=CascadeType.REMOVE) 
//	private List<ResponseFileDomain> responseFileDomain;
	
}
