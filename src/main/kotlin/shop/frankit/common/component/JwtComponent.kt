package shop.frankit.common.component

import shop.frankit.common.error.exception.FailAuthenticationException
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.Date

@Component
class JwtComponent(
    @Value("\${jwt.secret}")
    private var secretKey: String,

    @Value("\${jwt.expiration_time}")
    private var expirationTime: String
) {

    private val key: Key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))

    fun validateToken(token: String) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
        } catch (e: SecurityException) {
            throw FailAuthenticationException(cause = "invalid token")
        } catch(e: MalformedJwtException) {
            throw FailAuthenticationException(cause = "invalid token")
        } catch (e: ExpiredJwtException) {
            throw FailAuthenticationException(cause = "expired token")
        } catch (e: UnsupportedJwtException) {
            throw FailAuthenticationException(cause = "unsupported token")
        } catch (e: IllegalArgumentException) {
            throw FailAuthenticationException(cause = "empty claims")
        }
    }

    fun parseEmailByToken(token: String): String {
        validateToken(token)
        return parseClaims(token).get("email", String::class.java)
    }

    fun createToken(email: String): String {
        val claims = Jwts.claims()
        claims["email"] = email

        val now = Date()
        val expireTime = Date(now.time + expirationTime.toLong())

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(expireTime)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }


    fun parseClaims(accessToken: String): Claims {
        return try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).body
        } catch (e: ExpiredJwtException) {
            throw FailAuthenticationException(cause = "인증 만료")
        }
    }
}