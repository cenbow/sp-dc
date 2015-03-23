package kr.co.inogard.springboot.dc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

@SpringBootApplication
public class WebJspApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(WebJspApplication.class);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(WebJspApplication.class, args);
		System.out.println("Spring Boot Web Started.");
	}
	
}