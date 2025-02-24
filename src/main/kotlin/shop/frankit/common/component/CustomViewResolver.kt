package shop.frankit.common.component

import shop.frankit.common.dto.WebMemberDto
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import shop.frankit.common.error.exception.InvalidAuthIdException

class CustomViewResolver: HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean = parameter.parameterType == WebMemberDto::class.java

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any {
        try {
            val request = webRequest.nativeRequest as HttpServletRequest
            val id = request.getAttribute("id")

            val result = WebMemberDto(memberId = id as Long)

            return result

        } catch (ex: RuntimeException) {
            throw throw InvalidAuthIdException(message = ex.message)
        }
    }
}