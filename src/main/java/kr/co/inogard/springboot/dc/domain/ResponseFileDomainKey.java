package kr.co.inogard.springboot.dc.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class ResponseFileDomainKey implements Serializable {
	
	private static final long serialVersionUID = -5893570680474908072L;

	private String groupId;
	
	private int requestSeq;
	
	private Integer seq;
	
	private String url;
	
}
