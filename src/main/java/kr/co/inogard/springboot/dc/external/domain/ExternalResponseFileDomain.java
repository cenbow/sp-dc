package kr.co.inogard.springboot.dc.external.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="ExternalResponseFile")
public class ExternalResponseFileDomain implements Serializable {
	
	private static final long serialVersionUID = 873526847731037614L;

	@Id
	@Column(name="url")
	private String url;
	
	@Column(name="fileName", length=100)
	private String fileName;
	
	@Column(name="filePath", length=500)
	private String filePath;
	
	@ManyToOne
	@JoinColumns({
			@JoinColumn(name="url", referencedColumnName="annStdDoc1", nullable=false, insertable=false, updatable=false)
		})
	private ExternalResponseSFROA0802Domain externalResponseSFROA0802Domain;
}
