package com.stories.config;


import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Component
public class SwaggerConfig {

	@Bean
	public Docket SwaggerApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.stories"))
				.paths(PathSelectors.any())
				.build()
				.apiInfo(apiDetails());
	}

	private ApiInfo apiDetails() {
		
		@SuppressWarnings("deprecation")
		ApiInfo apiInfo = new ApiInfo(
				"Stories API",
				"Official documentation for Stories AP",
				"1.0",
				"Terms of service",
				"apache license",
				"http://stories-qa.us-east-2.elasticbeanstalk.com", 
				"API"
				);
		
		return apiInfo;
	}
	

	
}
