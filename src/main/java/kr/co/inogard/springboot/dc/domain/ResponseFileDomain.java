package kr.co.inogard.springboot.dc.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity(name="ResponseFile")
@Data
@Table(name="ResponseFile")
public class ResponseFileDomain implements Serializable {
	
	private static final long serialVersionUID = 6243217233785222129L;

	@Id
	@Column(name="url", length=400)
	private String url;
	
	@Column(name="fileName", length=100)
	private String fileName;
	
	@Column(name="filePath", length=500)
	private String filePath;
	
//	@ManyToOne
//	@JoinColumns({
//			@JoinColumn(name="url", referencedColumnName="annStdDoc1", unique=false, nullable=true, insertable=false, updatable=false)
//		})
//	private ResponseSFROA0802Domain responseSFROA0802Domain;
	
}