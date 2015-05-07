package kr.co.inogard.springboot.dc.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;

import kr.co.inogard.springboot.dc.domain.ResponseFileDomain;

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
public class ResponseFileItemReader extends JpaPagingItemReader<ResponseFileDomain> {
	
	private static final Logger log = LoggerFactory.getLogger(ResponseFileItemReader.class);
	
	@Autowired
	@Qualifier("datasourceOneEntityManager")
	private EntityManagerFactory datasourceOneEntityManager;
	
	@Value("#{jobParameters['groupId']}")
	private String groupId;
	
	@PostConstruct
	public void init() throws Exception{
    	Map<String, Object> mapParam = new HashMap<>();
    	mapParam.put("groupId", groupId);
    	
//    	JpaNativeQueryProvider<ResponseFileDomain> jpaNativeQueryProvider= new JpaNativeQueryProvider<ResponseFileDomain>();
//    	jpaNativeQueryProvider.setSqlQuery("SELECT * FROM ResponseFile WHERE groupId=:groupId");
//    	jpaNativeQueryProvider.setSqlQuery("SELECT * FROM ResponseFile");
//    	jpaNativeQueryProvider.setEntityClass(ResponseFileDomain.class);
//    	jpaNativeQueryProvider.afterPropertiesSet();
    	
    	CriteriaBuilder cb = datasourceOneEntityManager.getCriteriaBuilder();
    	CriteriaQuery<ResponseFileDomain> cq = cb.createQuery(ResponseFileDomain.class);
    	Root root = cq.from(ResponseFileDomain.class);
    	EntityType<ResponseFileDomain> model = root.getModel();
    	cq.select(root).where(cb.isNull(root.get(model.getSingularAttribute("fileName", String.class)))
    			, cb.isNull(root.get(model.getSingularAttribute("filePath", String.class))));
    	TypedQuery<ResponseFileDomain> tq = datasourceOneEntityManager.createEntityManager().createQuery(cq);
    	String queryString = tq.unwrap(org.hibernate.Query.class).getQueryString(); 
    	log.debug(queryString);
    	
		super.setEntityManagerFactory(datasourceOneEntityManager);
//		super.setQueryProvider(jpaNativeQueryProvider);
//		super.setParameterValues(mapParam);
		super.setQueryString(queryString);
		super.setPageSize(10);
		super.afterPropertiesSet();
		super.setSaveState(true);
	}
	
}
