package kr.co.inogard.springboot.dc.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;

import kr.co.inogard.springboot.dc.domain.ResponseSFROA0802Domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@StepScope
public class ResponseSFROA0802ItemReader extends JpaPagingItemReader<ResponseSFROA0802Domain> {
	
	private static final Logger log = LoggerFactory.getLogger(ResponseSFROA0802ItemReader.class);
	
	@Autowired
	@Qualifier("datasourceOneEntityManager")
	private EntityManagerFactory datasourceOneEntityManager;
	
	@Value("#{jobParameters['groupId']}")
	private String groupId;
	
	@PostConstruct
	public void init() throws Exception{
    	Map<String, Object> mapParam = new HashMap<>();
    	mapParam.put("transferYn", "Y");
    	
    	CriteriaBuilder cb = datasourceOneEntityManager.getCriteriaBuilder();
    	CriteriaQuery<ResponseSFROA0802Domain> cq = cb.createQuery(ResponseSFROA0802Domain.class);
    	Root root = cq.from(ResponseSFROA0802Domain.class);
    	EntityType<ResponseSFROA0802Domain> model = root.getModel();
    	ParameterExpression<String> transferYn = cb.parameter(String.class, "transferYn");
    	cq.select(root).where(cb.notEqual(root.get("transferYn"), transferYn));
    	TypedQuery<ResponseSFROA0802Domain> tq = datasourceOneEntityManager.createEntityManager().createQuery(cq);
    	String queryString = tq.unwrap(org.hibernate.Query.class).getQueryString(); 
    	log.debug(queryString);
    	
		super.setEntityManagerFactory(datasourceOneEntityManager);
		super.setParameterValues(mapParam);
		super.setQueryString(queryString);
		super.setPageSize(10);
		super.afterPropertiesSet();
		super.setSaveState(true);
	}
	
}
