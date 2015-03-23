package kr.co.inogard.springboot.dc.config;

import java.nio.charset.Charset;

import javax.servlet.Filter;

import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
public class SpringConfig {
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	@Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        return new StringHttpMessageConverter(Charset.forName("UTF-8"));
    }
 
    @Bean
    public Filter characterEncodingFilter() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }
	
	@Bean
    public EmbeddedServletContainerCustomizer containerCustomizer(){
        return new TomcatContainerCustomizer(tomcatJspServletConfig());
    }
	
    @Bean
    public TomcatJspServletConfig tomcatJspServletConfig() {
        return new TomcatJspServletConfig();
    }

    static class TomcatContainerCustomizer implements EmbeddedServletContainerCustomizer {

        private TomcatJspServletConfig tomcatJspServletConfig;

        public TomcatContainerCustomizer(TomcatJspServletConfig tomcatJspServletConfig) {
            this.tomcatJspServletConfig = tomcatJspServletConfig;
        }

        @Override
        public void customize(ConfigurableEmbeddedServletContainer container) {

        	if (container instanceof TomcatEmbeddedServletContainerFactory) {
                TomcatEmbeddedServletContainerFactory tomcatContainer = (TomcatEmbeddedServletContainerFactory) container;
                tomcatContainer.addContextCustomizers(tomcatJspServletConfig);
            }
        	
            ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/401.html");
            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404.html");
            ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500.html");
 
            container.addErrorPages(error401Page, error404Page, error500Page);
        }
    }

    @PropertySource("application.properties")
    public static class TomcatJspServletConfig implements TomcatContextCustomizer {

    	@Value("${jsp.development.mode}")
    	private String jspDevelopmentMode;
    	
        @Override
        public void customize(Context context) {
            // TomcatEmbeddedServletContainerFactory가 설정한 JspServlet의 이름이 "jsp"
            Wrapper jsp = (Wrapper)context.findChild("jsp");
            
            // JspServlet의 개발모드를 활성화 여부
            System.out.println("jspDevelopmentMode : " + jspDevelopmentMode);
            jsp.addInitParameter("development", jspDevelopmentMode);
        }
    }
}
