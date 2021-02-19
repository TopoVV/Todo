package com.topov.todo.config.apidoc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.SpringfoxWebMvcConfiguration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;


@Configuration
@EnableSwagger2

@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {
    private static final Logger log = LogManager.getLogger(SwaggerConfig.class);
    @Bean
    public Docket api()
    {
        log.info("Docket initialization");
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.regex("/.*"))
            .build().apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
            "Todo REST API",
            "I don't know what to write here.",
            "Alpha",
            "Terms of service",
            new Contact("Vlad Topov", "www.todo.com", "vladtopov2001@gmail.com"),
            "License of API", "API license URL", Collections.emptyList());
    }
}
