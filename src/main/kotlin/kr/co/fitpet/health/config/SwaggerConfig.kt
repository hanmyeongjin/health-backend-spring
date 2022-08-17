package kr.co.fitpet.health.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springdoc.core.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class SwaggerConfig {

    @Bean
    fun healthApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("health-backend-spring")
            .pathsToMatch("/health/**", "/registration/**")
            .build()
    }

    @Bean
    fun healthBackendApi(): OpenAPI {
        return OpenAPI()
            .components(
                Components()
                    .addSecuritySchemes(
                        "UserToken",
                        SecurityScheme()
                            .type(SecurityScheme.Type.APIKEY)
                            .name("Authorization")
                            .`in`(SecurityScheme.In.HEADER)
                    )
            )
            .info(
                Info()
                    .title(API_NAME)
                    .description("$API_NAME $API_VERSION")
                    .version(API_VERSION)
            ).addSecurityItem(
                SecurityRequirement()
                    .addList("UserToken", listOf("read", "write"))
            )
    }

    companion object {
        private const val API_NAME = "health-backend-spring API"
        private const val API_VERSION = "0.0.1"
    }
}