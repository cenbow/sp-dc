package kr.co.inogard.springboot.dc.repository;

import java.util.List;

import kr.co.inogard.springboot.dc.domain.ResponseFileDomain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ResponseFileRepository extends JpaRepository<ResponseFileDomain, String> {
	
	@Query("SELECT a FROM ResponseFile a WHERE fileName != '' AND filePath != ''")
    public List<ResponseFileDomain> findByNotEmptyFilenameAndFilepath();
	
}
