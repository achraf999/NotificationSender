package com.smsSender.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

import com.fasterxml.classmate.TypeResolver;
import com.smsSender.server.entities.EmailNotification;
import com.smsSender.server.entities.SmsNotification;
import com.smsSender.server.entities.WhatsAppNotification;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SpringFoxConfig {
	
	 @Bean
	    public Docket api() {
		 TypeResolver typeResolver = new TypeResolver();
	        return new Docket(DocumentationType.SWAGGER_2)
	        		.additionalModels(
	        				typeResolver.resolve(EmailNotification.class),
	        				typeResolver.resolve(SmsNotification.class),
	        				typeResolver.resolve(WhatsAppNotification.class)
	        				)
	        		.apiInfo(apiInfo())
	                .select()
	                .apis(RequestHandlerSelectors.basePackage("com.smsSender.server.controllers"))
	                .paths(PathSelectors.any())
	                .build();
		}

		private ApiInfo apiInfo() {
			return new ApiInfoBuilder()
					.title("Notifications Management Rest APIs")
					.description("This page lists all the rest apis for Notifications Management App.")
					.version("1.0-SNAPSHOT").contact(new Contact("youssed idbensalah", "BRATIL ", "youssefidbensaleh@gmail.com"))
					.build();
		}
		
		
}
