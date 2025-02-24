package shop.frankit.common.component

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import shop.frankit.common.error.exception.InvalidAuthTokenException
import shop.frankit.domain.member.service.MemberQueryBaseService

@Component
class AuthInterceptor(
    private val jwtComponent: JwtComponent,
    private val memberQueryBaseService: MemberQueryBaseService
): HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val email = try {
            request.getHeader("Authorization")
                .replace("Bearer ", "")
                .let(jwtComponent::parseEmailByToken)

        } catch (ex: RuntimeException) {
            throw InvalidAuthTokenException(token = request.getHeader("Authorization"))
        }

        memberQueryBaseService
            .findMemberByEmailOrThrow(email = email)
            .let { request.setAttribute("id", it.id) }

        return true
    }
}