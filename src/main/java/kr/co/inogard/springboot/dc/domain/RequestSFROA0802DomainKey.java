package kr.co.inogard.springboot.dc.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class RequestSFROA0802DomainKey implements Serializable {

	private static final long serialVersionUID = 6482117714384094058L;

	private String groupId;
	
	private Integer requestSeq;
	
}
