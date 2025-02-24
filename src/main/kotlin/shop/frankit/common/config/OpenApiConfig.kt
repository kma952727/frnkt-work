package shop.frankit.common.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun openApi(): OpenAPI {
        return OpenAPI()
                    .info(
                        Info().apply {
                            title = "프랜킷 상품 관리 API"
                            description = "상품, 옵션 관리(추가, 수정, 삭제)를 지원합니다."
                            version = "1.0.0"
                        }
                    )
    }
}