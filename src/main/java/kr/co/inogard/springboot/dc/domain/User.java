package kr.co.inogard.springboot.dc.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lombok.Data;

@Entity	
@Data
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column(name="name", length=50)
	private String name;
	
	@Temporal(TemporalType.DATE)
	private Date joinDate;
	
	@Enumerated
	private MaleFemale maleFemale;
	
	@Transient
	private String auth;
	
}
