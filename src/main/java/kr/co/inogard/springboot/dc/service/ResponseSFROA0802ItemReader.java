package kr.co.inogard.springboot.dc.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;

import kr.co.inogard.springboot.dc.domain.ResponseSFROA0802Domain;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.orm.JpaNativeQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@StepScope
public class ResponseSFROA0802ItemReader extends JpaPagingItemReader<ResponseSFROA0802Domain> {
	
	@Autowired
	@Qualifier("datasourceOneEntityManager")
	private EntityManagerFactory datasourceOneEntityManager;
	
	@Value("#{jobParameters['groupId']}")
	private String groupId;
	
	@PostConstruct
	public void init() throws Exception{
    	Map<String, Object> mapParam = new HashMap<>();
    	mapParam.put("groupId", groupId);
		System.out.println("====================================");
		System.out.println("====================================");
		System.out.println("groupId = " + groupId);
		System.out.println("====================================");
		System.out.println("====================================");
    	
    	JpaNativeQueryProvider<ResponseSFROA0802Domain> jpaNativeQueryProvider= new JpaNativeQueryProvider<ResponseSFROA0802Domain>();
//    	jpaNativeQueryProvider.setSqlQuery("SELECT * FROM ResponseSFROA0802 WHERE groupId=:groupId");
    	jpaNativeQueryProvider.setSqlQuery("SELECT * FROM ResponseSFROA0802 WHERE transferYn != 'Y'");
    	jpaNativeQueryProvider.setEntityClass(ResponseSFROA0802Domain.class);
    	jpaNativeQueryProvider.afterPropertiesSet();
    	
		super.setEntityManagerFactory(datasourceOneEntityManager);
		super.setQueryProvider(jpaNativeQueryProvider);
//		super.setParameterValues(mapParam);
		super.setPageSize(10);
		super.afterPropertiesSet();
		super.setSaveState(true);
	}
	
}
