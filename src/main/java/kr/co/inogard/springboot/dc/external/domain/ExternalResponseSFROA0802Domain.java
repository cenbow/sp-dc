package kr.co.inogard.springboot.dc.external.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="ExternalResponseSFROA0802")
public class ExternalResponseSFROA0802Domain implements Serializable {
	
	private static final long serialVersionUID = -7707863697246973180L;

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
	
	@OneToMany(fetch=FetchType.LAZY
			, mappedBy="externalResponseSFROA0802Domain"
			, cascade=CascadeType.REMOVE) 
	private List<ExternalResponseFileDomain> externalResponseFileDomain;
	
}
