package kr.co.inogard.springboot.dc.repository;

import java.util.List;

import kr.co.inogard.springboot.dc.domain.RequestSFROA0802Domain;
import kr.co.inogard.springboot.dc.domain.RequestSFROA0802DomainKey;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RequestSFROA0802Repository extends JpaRepository<RequestSFROA0802Domain, RequestSFROA0802DomainKey> {

	@Query("SELECT a FROM RequestSFROA0802 a WHERE groupId != :groupId AND sDate = :sDate AND eDate = :eDate AND orderCode = :orderCode AND jobExecutionStatus = 'COMPLETED' ORDER BY groupId Desc")
    public List<RequestSFROA0802Domain> findBySDateAndEDateAndOrderCode(@Param("groupId") String groupId
    		, @Param("sDate") String sDate
    		, @Param("eDate") String eDate
    		, @Param("orderCode") String orderCode);
	
	@Query("SELECT a FROM RequestSFROA0802 a WHERE jobExecutionId = :jobExecutionId")
    public RequestSFROA0802Domain findByJobExcutionId(@Param("jobExecutionId") long jobExecutionId);
	
}
