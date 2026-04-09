package tender.ma.medicalapplied.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация Springdoc
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Medical Applied API",
                version = "1.0",
                description = "REST API для Medical Applied"
        ),
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SpringdocConfiguration {
//    @Value("${docshouse.gateway-path}")
//    private String dHGatewayPath;
//    @Value("${docshouse.keycloak.external-token-uri}")
//    private String tokenUri;
//    @Value("${yml-vars.route.path}")
//    private String routePath;
//
//    @Bean
//    public OpenAPI customOpenAPI() {
//        /*
//         Определение глобальной security scheme используемой по-умолчанию для всех ednpoint'ов в swagger.
//         Для переопределения схемы (в том числе для разблокировки endpoint'а в swagger) нужно использовать
//         @SecurityRequirement для конкретного endpoint'а.
//         См. https://springdoc.org/#how-do-i-add-authorization-header-in-requests
//         */
//        var securitySchemeName = "keycloak";
//        var securityItem = new SecurityRequirement().addList(securitySchemeName);
//        var globalSecurityScheme = new SecurityScheme()
//                .type(SecurityScheme.Type.OAUTH2)
//                .scheme("bearer")
//                .bearerFormat("jwt")
//                .in(SecurityScheme.In.HEADER)
//                .name("Authorization")
//                .flows(new OAuthFlows().password(new OAuthFlow()
//                        .tokenUrl(tokenUri)));
//
//        return new OpenAPI()
//                .addSecurityItem(securityItem)
//                .components(new Components()
//                        .addSecuritySchemes(securitySchemeName, globalSecurityScheme));
//    }
//
//    @Bean
//    public OpenApiCustomizer gatewayServerCustomizer() {
//        if (StringUtils.isBlank(dHGatewayPath)) {
//            return ignored -> {
//            };
//        } else {
//            String swaggerGatewayUrl = dHGatewayPath + routePath.substring(0, routePath.lastIndexOf("/"));
//            return openApi -> openApi
//                    .setServers(
//                            List.of(
//                                    new Server().url(swaggerGatewayUrl)
//                            )
//                    );
//        }
//    }
}
