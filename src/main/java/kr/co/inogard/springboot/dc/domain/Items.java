package kr.co.inogard.springboot.dc.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="items")
public class Items<T> {

	private List<Class<T>> item;

	@XmlElementRefs({
		@XmlElementRef(type=ResponseSFROA0801.class),
		@XmlElementRef(type=ResponseSFROA0802.class)
	})
	public List<Class<T>> getItem() {
		return item;
	}

	public void setItem(List<Class<T>> item) {
		this.item = item;
	}
	
}
