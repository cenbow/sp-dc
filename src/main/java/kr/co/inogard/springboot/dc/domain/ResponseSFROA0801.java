package kr.co.inogard.springboot.dc.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.ToString;

@XmlRootElement(name="item")
@ToString
public class ResponseSFROA0801 {

	private String item;

	@XmlElement(name="��ǰ")
	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}
	
}
