package kr.co.inogard.springboot.dc.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
		basePackages="kr.co.inogard.springboot.dc.repository",
		transactionManagerRef="datasourceOneTransactionManager",
		entityManagerFactoryRef="datasourceOneEntityManager")
public class DatasourceOneConfig {
	
	@Value("${datasource.one.jpa.database-platform}")
	private String databasePlatform;
	
	@Value("${datasource.one.jpa.hbm2ddl.auto}")
	private String hbm2ddlAuto;
	
	@Value("${datasource.one.jpa.showSql}")
	private String showSql;

	@Primary
	@Bean(name="datasourceOneDataSource")
	@ConfigurationProperties(prefix="datasource.one")
	public DataSource datasourceOneDataSource(){
	    return DataSourceBuilder
	            .create()
	            .build();
	}

	@Primary
	@Bean(name="datasourceOneEntityManager")
	public LocalContainerEntityManagerFactoryBean datasourceOneEntityManagerFactory(){       
		
		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(datasourceOneDataSource());
		em.setPackagesToScan(new String[] {"kr.co.inogard.springboot.dc.domain"});
		em.setJpaVendorAdapter(vendorAdapter);
		em.setJpaProperties(additionalJpaProperties());
		em.setPersistenceUnitName("datasourceOne");
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
	
	@Primary
	@Bean(name="datasourceOneTransactionManager")
	public PlatformTransactionManager transactionManager() {
		JpaTransactionManager txManager = new JpaTransactionManager();
	    txManager.setEntityManagerFactory(datasourceOneEntityManagerFactory().getObject());
	    return txManager;
	}
	
}
