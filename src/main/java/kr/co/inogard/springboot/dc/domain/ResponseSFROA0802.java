package kr.co.inogard.springboot.dc.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.ToString;

@XmlRootElement(name="item")
@ToString
public class ResponseSFROA0802 {
	
	private String bidNo;
	
	private String supplyDate;
	
	private String eachLicenseAllowTypeOfBusiness;
	
	private String orderOrgCode;
	
	private String orderOrgNm;
	
	@XmlElement(name="입찰공고번호")
	public String getBidNo() {
		return bidNo;
	}

	public void setBidNo(String bidNo) {
		this.bidNo = bidNo;
	}

	@XmlElement(name="납품일수")
	public String getSupplyDate() {
		return supplyDate;
	}

	public void setSupplyDate(String supplyDate) {
		this.supplyDate = supplyDate;
	}

	@XmlElement(name="각면허별허용업종")
	public String getEachLicenseAllowTypeOfBusiness() {
		return eachLicenseAllowTypeOfBusiness;
	}

	public void setEachLicenseAllowTypeOfBusiness(
			String eachLicenseAllowTypeOfBusiness) {
		this.eachLicenseAllowTypeOfBusiness = eachLicenseAllowTypeOfBusiness;
	}

	@XmlElement(name="발주기관코드")
	public String getOrderOrgCode() {
		return orderOrgCode;
	}

	public void setOrderOrgCode(String orderOrgCode) {
		this.orderOrgCode = orderOrgCode;
	}

	@XmlElement(name="발주기관")
	public String getOrderOrgNm() {
		return orderOrgNm;
	}

	public void setOrderOrgNm(String orderOrgNm) {
		this.orderOrgNm = orderOrgNm;
	}
	
	
	
}