package shop.frankit.common.config

import shop.frankit.common.component.AuthInterceptor
import shop.frankit.common.component.CustomViewResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val authInterceptor: AuthInterceptor
): WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(CustomViewResolver())
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authInterceptor)
            .order(1)
            .excludePathPatterns(
                "/api/members/sign-up",
                "/api/members/login",
                "/swagger-ui/**",
                "/swagger-resources/**",
                "/v3/api-docs/**"
            )
    }
}