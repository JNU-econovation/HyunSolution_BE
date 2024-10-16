package com.hyunsolution.dangu.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import javax.servlet.ServletContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {
    private static final String SWAGGER_DOCS_VERSION = "1.0.0";

    @Bean
    public OpenAPI openAPI(ServletContext servletContext) {
        String contextPath = servletContext.getContextPath();
        Server server = new Server().url(contextPath);
        return new OpenAPI()
                .servers(List.of(server))
                .components(authSetting())
                .info(swaggerInfo())
                .addSecurityItem(
                        new SecurityRequirement()
                                .addList("Authorization")); // SecurityRequirement 추가
    }

    private Info swaggerInfo() {
        License license = new License();
        license.setUrl("https://github.com/JNU-econovation/HyunSolution_BE.git");
        license.setName("HyunSolution");
        return new Info()
                .version(SWAGGER_DOCS_VERSION)
                .title("\"HyunSolution 서버 API문서\"")
                .description("HyunSolution 서버의 API 문서 입니다.")
                .license(license);
    }

    private Components authSetting() {
        return new Components()
                .addSecuritySchemes(
                        "Authorization",
                        new SecurityScheme().type(Type.APIKEY).in(In.HEADER).name("Authorization"));
    }
}
