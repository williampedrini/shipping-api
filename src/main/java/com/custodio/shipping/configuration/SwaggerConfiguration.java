package com.custodio.shipping.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.any;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@Configuration
@EnableSwagger2
class SwaggerConfiguration {

    /**
     * Build the primary interface used by Springfox framework.
     * @return The build primary interface.
     */
    @Bean
    Docket api() {
        return new Docket(SWAGGER_2)
                .useDefaultResponseMessages(false)
                .apiInfo(buildApiInfo())
                .select()
                .apis(basePackage("com.custodio.shipping"))
                .paths(any())
                .build();
    }

    /**
     * Build the general api information.
     * @return The built general api information.
     */
    private ApiInfo buildApiInfo() {
        return new ApiInfoBuilder()
                .title("Shipping API")
                .build();
    }
}
