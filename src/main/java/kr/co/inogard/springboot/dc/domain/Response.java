package kr.co.inogard.springboot.dc.domain;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="response")
public class Response {
	
	private Header header;
	
	private Body body;
	
	@XmlElementRef
	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	@XmlElementRef
	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}
}
