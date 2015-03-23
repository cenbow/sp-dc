package kr.co.inogard.springboot.dc.repository;

import java.util.List;

import kr.co.inogard.springboot.dc.domain.MaleFemale;
import kr.co.inogard.springboot.dc.domain.User;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

	List<User> findByMaleFemale(MaleFemale maleFemale);
	
}
