package kr.co.inogard.springboot.dc.external.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class ExternalResponseSFROA0802DomainKey implements Serializable {

	private static final long serialVersionUID = 6305577892477576172L;

	private String groupId;
	
	private int requestSeq;

	private Integer seq;
	
}
