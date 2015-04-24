package kr.co.inogard.springboot.dc.repository;

import kr.co.inogard.springboot.dc.domain.ResponseFileDomain;
import kr.co.inogard.springboot.dc.domain.ResponseFileDomainKey;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ResponseFileRepository extends JpaRepository<ResponseFileDomain, ResponseFileDomainKey> {
	
}
