package kr.co.inogard.springboot.dc.repository;

import kr.co.inogard.springboot.dc.domain.OpenapiBatchJobMapperDomain;
import kr.co.inogard.springboot.dc.domain.OpenapiBatchJobMapperDomainKey;

import org.springframework.data.repository.CrudRepository;

public interface OpenapiBatchJobMapperRepository extends CrudRepository<OpenapiBatchJobMapperDomain, OpenapiBatchJobMapperDomainKey> {

}
