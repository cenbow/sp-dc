package kr.co.inogard.springboot.dc.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		basePackages="kr.co.inogard.springboot.dc.external.repository",
		transactionManagerRef = "datasourceTwoTransactionManager",    
		entityManagerFactoryRef = "datasourceTwoEntityManager")
public class DatasourceTwoConfig {
	
	@Value("${datasource.two.jpa.database-platform}")
	private String databasePlatform;
	
	@Value("${datasource.two.jpa.hbm2ddl.auto}")
	private String hbm2ddlAuto;
	
	@Value("${datasource.two.jpa.showSql}")
	private String showSql;

	@Bean(name="datasourceTwoDataSource")
	@ConfigurationProperties(prefix="datasource.two")
	public DataSource datasourceTwoDataSource(){
	    return DataSourceBuilder
	            .create()
	            .build();
	}

	@Bean(name="datasourceTwoEntityManager")
	public LocalContainerEntityManagerFactoryBean datasourceTwoEntityManagerFactory(){       
		
		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(datasourceTwoDataSource());
		em.setPackagesToScan(new String[] {"kr.co.inogard.springboot.dc.external.domain"});
		em.setJpaVendorAdapter(vendorAdapter);
		em.setJpaProperties(additionalJpaProperties());
		em.setPersistenceUnitName("datasourceTwo");
		em.afterPropertiesSet();
		
		return em;
	} 
	
	Properties additionalJpaProperties(){
		Properties properties = new Properties();
		properties.setProperty("hibernate.hbm2ddl.auto", hbm2ddlAuto);
		properties.setProperty("hibernate.dialect", databasePlatform);
		properties.setProperty("hibernate.show_sql", showSql);
		return properties;
	}
	
	@Bean(name="datasourceTwoTransactionManager")
	public PlatformTransactionManager transactionManager() {
		JpaTransactionManager txManager = new JpaTransactionManager();
	    txManager.setEntityManagerFactory(datasourceTwoEntityManagerFactory().getObject());
	    return txManager;
	}
	
}
