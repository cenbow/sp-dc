package kr.co.inogard.springboot.dc.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="item")
public class Item {
	
	private String supplyDate;
	
	private String eachLicenseAllowTypeOfBusiness;

	@XmlElement(name="��ǰ�ϼ�")
	public String getSupplyDate() {
		return supplyDate;
	}

	public void setSupplyDate(String supplyDate) {
		this.supplyDate = supplyDate;
	}

	@XmlElement(name="�����㺰������")
	public String getEachLicenseAllowTypeOfBusiness() {
		return eachLicenseAllowTypeOfBusiness;
	}

	public void setEachLicenseAllowTypeOfBusiness(
			String eachLicenseAllowTypeOfBusiness) {
		this.eachLicenseAllowTypeOfBusiness = eachLicenseAllowTypeOfBusiness;
	}
	
}
