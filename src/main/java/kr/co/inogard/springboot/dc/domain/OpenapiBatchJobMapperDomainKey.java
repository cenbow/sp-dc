package kr.co.inogard.springboot.dc.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class OpenapiBatchJobMapperDomainKey implements Serializable {

	private static final long serialVersionUID = 4796760782591891291L;

	private long jobExecutionId;
	
	private int openApiId;
	
}
