package shop.frankit.common.component

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Aspect
@Component
class LogAspect {
    private val log = LoggerFactory.getLogger(LogAspect::class.java)

    @Pointcut("execution(* shop.frankit.domain..controller..*(..))")
    private fun callController() {}

    @Around("callController()")
    fun logController(joinPoint: ProceedingJoinPoint): Any? {
        val request = (RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes)?.request

        val path = request?.requestURI
        val method = request?.method
        val param = request?.queryString
        val headers = request?.headerNames?.asSequence()?.associateWith { request.getHeader(it) } ?: emptyMap()
        val body = request?.inputStream?.bufferedReader()?.use { it.readText() }

        try {
            log.info("""
                start call : $path 
                method : $method
                param : $param
                headers : $headers
                body : $body"""
            )

            val result = joinPoint.proceed()

            log.info("end call $path, isSuccess : true, result : $result")

            return result
        } catch (ex: Exception) {
            log.info("end call : $path, isSuccess : false, error : $ex")
            throw ex
        }
    }
}