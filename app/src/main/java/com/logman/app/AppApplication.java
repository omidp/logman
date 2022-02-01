package com.logman.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean corsServletFilter() {

		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new MDCServletFilter());
		registration.addUrlPatterns("/*");
		registration.setName("mdcAwareFilter");
		return registration;
	}

}
