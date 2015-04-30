package kr.co.inogard.springboot.dc.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;

import kr.co.inogard.springboot.dc.domain.ResponseFileDomain;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.orm.JpaNativeQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@StepScope
public class ResponseFileItemReader extends JpaPagingItemReader<ResponseFileDomain> {
	
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
    	
    	JpaNativeQueryProvider<ResponseFileDomain> jpaNativeQueryProvider= new JpaNativeQueryProvider<ResponseFileDomain>();
//    	jpaNativeQueryProvider.setSqlQuery("SELECT * FROM ResponseFile WHERE groupId=:groupId");
    	jpaNativeQueryProvider.setSqlQuery("SELECT * FROM ResponseFile");
    	jpaNativeQueryProvider.setEntityClass(ResponseFileDomain.class);
    	jpaNativeQueryProvider.afterPropertiesSet();
    	
		super.setEntityManagerFactory(datasourceOneEntityManager);
		super.setQueryProvider(jpaNativeQueryProvider);
//		super.setParameterValues(mapParam);
		super.setPageSize(10);
		super.afterPropertiesSet();
		super.setSaveState(true);
	}
	
}
