package cz.muni.pa165.component;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.stereotype.Component;

/**
 * Api customizer for api keys.
 */
@Component
public class CustomOpenApiCustomizer implements OpenApiCustomizer {

  @Override
  public void customise(OpenAPI openApi) {
    openApi
        .components(new Components()
            .addSecuritySchemes("apiKeyScheme", new SecurityScheme()
                .in(SecurityScheme.In.HEADER)
                .type(SecurityScheme.Type.APIKEY)
                .name("API_KEY"))
            .schemas(openApi.getComponents().getSchemas())
        )
        .addSecurityItem(new SecurityRequirement()
            .addList("apiKeyScheme")
        );
  }
}
