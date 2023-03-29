package com.librbary.main.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OAS3Config {

  @Bean
  public OpenAPI openAPIConfig() {
    return new OpenAPI().info(apiInfo()).schemaRequirement("ApiSecurity", getSecurityScheme());
  }

  public Info apiInfo() {
    var info = new Info();
    info.title("liBRBary Backend APIs").description("liBRBary - Buy, Rental, Barter").version("v1")
        .contact(new Contact().name("liBRBary Support Team").email("support@librbary.com"));

    return info;
  }

  @Bean
  public GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder().group("liBRBary-Api").pathsToMatch("/v1/**").build();
  }

  public SecurityScheme getSecurityScheme() {
    return new SecurityScheme().name("Authorization").type(SecurityScheme.Type.APIKEY).in(SecurityScheme.In.HEADER);
  }
}
