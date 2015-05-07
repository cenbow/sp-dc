package kr.co.inogard.springboot.dc.domain;

import java.util.List;

public class OpenAPIRequest<T> {
	
	private String groupId;
	
	private int requestSeq;
	
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
	
}
