package com.oy.secondskill.common.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
public class SwaggerConfig {
    @Bean
    public Docket userApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("秒杀案例").apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.oy.secondskill.web")).paths(PathSelectors.any()).build();
    }
    // 预览地址:swagger-ui.html
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Spring 中使用Swagger2构建文档").termsOfServiceUrl("https://github.com/OyXw1")
                .contact(new Contact("尚想网 ", "https://github.com/OyXw1/", "992543768@qq.com")).version("1.1").build();
    }
}
