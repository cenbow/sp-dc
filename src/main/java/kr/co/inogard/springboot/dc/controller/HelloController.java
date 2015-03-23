package kr.co.inogard.springboot.dc.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.co.inogard.springboot.dc.domain.MaleFemale;
import kr.co.inogard.springboot.dc.domain.User;
import kr.co.inogard.springboot.dc.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	
	@Autowired
	UserRepository userRepository;

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }
    
    @RequestMapping("/users")
    public List<User> users() {
    	
    	List<User> list = new ArrayList<>();
    	
    	User user = new User();
    	user.setName("sun");
    	user.setJoinDate(new Date());
    	user.setMaleFemale(MaleFemale.MALE);
    	user.setAuth("1");
    	
    	userRepository.save(user);
    	
    	user = new User();
    	user.setName("moon");
    	user.setJoinDate(new Date());
    	user.setMaleFemale(MaleFemale.FEMALE);
    	user.setAuth("1");
    	
    	userRepository.save(user);
    	
    	for(User User : userRepository.findAll()) {
            System.out.println(User);
            list.add(User);
        }
    	
    	list.clear();
    	for(User User : userRepository.findByMaleFemale(MaleFemale.MALE)) {
            System.out.println(User);
            list.add(User);
        }
    	
        return list;
    }

}