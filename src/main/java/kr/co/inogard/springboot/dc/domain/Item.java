package kr.co.inogard.springboot.dc.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="item")
public class Item {
	
	private String supplyDate;
	
	private String eachLicenseAllowTypeOfBusiness;

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
	
}
