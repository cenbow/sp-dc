package kr.co.inogard.springboot.dc.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;

@Entity(name="OpenapiBatchJobMapper")
@Data
@IdClass(OpenapiBatchJobMapperDomainKey.class)
@Table(name="OpenapiBatchJobMapper")
public class OpenapiBatchJobMapperDomain {
	
	@Id
	private long jobExecutionId;
	
	@Id
	private int openApiId;
	
}
