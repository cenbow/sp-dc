package kr.co.inogard.springboot.dc.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import kr.co.inogard.springboot.dc.service.AnnStdDocAdapter;
import lombok.ToString;

@XmlRootElement(name="item")
@ToString
public class ResponseSFROA0802 {
	
	private String groupId;
	
	private int requestSeq;
	
	private int seq;
	
	private String bidNo;
	
	private String supplyDate;
	
	private String eachLicenseAllowTypeOfBusiness;
	
	private String orderOrgCode;
	
	private String orderOrgNm;
		
	private String annStdDoc1; 
	
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public int getRequestSeq() {
		return requestSeq;
	}

	public void setRequestSeq(int requestSeq) {
		this.requestSeq = requestSeq;
	}
	
	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}
	
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

	@XmlElement(name="공고규격서1")
	@XmlJavaTypeAdapter(value=AnnStdDocAdapter.class)
	public String getAnnStdDoc1() {
		return annStdDoc1;
	}

	public void setAnnStdDoc1(String annStdDoc1) {
		this.annStdDoc1 = annStdDoc1;
	}

}