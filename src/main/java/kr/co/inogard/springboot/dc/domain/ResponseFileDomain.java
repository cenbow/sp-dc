package kr.co.inogard.springboot.dc.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@IdClass(ResponseFileDomainKey.class)
@Table(name="ResponseFile")
public class ResponseFileDomain {

	@Id
	@Column(name="groupId")
	private String groupId;
	
	@Id
	@Column(name="requestSeq")
	private int requestSeq;
	
	@Id
	@Column(name="seq")
	private Integer seq;
	
	@Id
	@Column(name="url", length=400)
	private String url;
	
	@Column(name="fileName", length=100)
	private String fileName;
	
	@Column(name="filePath", length=500)
	private String filePath;
	
	@ManyToOne
	@JoinColumns({
			@JoinColumn(name="groupId", referencedColumnName="groupId", nullable=false, insertable=false, updatable=false), 
			@JoinColumn(name="requestSeq", referencedColumnName="requestSeq", nullable=false, insertable=false, updatable=false),
			@JoinColumn(name="seq", referencedColumnName="seq", nullable=false, insertable=false, updatable=false)
		})
	private ResponseSFROA0802Domain responseSFROA0802Domain;
	
}