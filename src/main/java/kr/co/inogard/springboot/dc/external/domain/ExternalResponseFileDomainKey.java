package kr.co.inogard.springboot.dc.external.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class ExternalResponseFileDomainKey implements Serializable {
	
	private static final long serialVersionUID = -5893570680474908072L;

	private String groupId;
	
	private int requestSeq;
	
	private Integer seq;
	
	private String url;
	
}
