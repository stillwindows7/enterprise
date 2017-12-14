package io.github.enterprise.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by Sheldon on 2017年12月14日
 *
 */
@Configuration
@EnableSwagger2
public class Swagger2Configuration {

	@Bean
	public Docket buildDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(buildApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("io.github.enterprise.web"))
                .paths(PathSelectors.any())
                .build();
	}
	
	private ApiInfo buildApiInfo() {
        return new ApiInfoBuilder()
                .title("API 接口文档")
                .license("Sheldon Chen")
                .version("1.0")
                .build();
	}
	
}
