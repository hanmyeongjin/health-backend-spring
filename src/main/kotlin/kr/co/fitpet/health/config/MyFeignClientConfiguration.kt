package kr.co.fitpet.health.config

import feign.Response
import feign.codec.ErrorDecoder
import kr.co.fitpet.health.exception.AuthorizationException
import kr.co.fitpet.health.exception.RestApiServerException
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus


@Configuration
class UserFeignClientConfiguration {

    @Bean
    fun errorDecoder(): ErrorDecoder? {
        return CustomErrorDecoder()
    }
}

class CustomErrorDecoder : ErrorDecoder {
    override fun decode(methodKey: String?, response: Response): Exception {
//        val requestUrl: String = response.request().url()
//        val responseBody: Response.Body = response.body()
        val responseStatus: HttpStatus = HttpStatus.valueOf(response.status())
        return if (responseStatus.is5xxServerError) {
            RestApiServerException()
        } else if (responseStatus.is4xxClientError) {
            AuthorizationException()
        } else {
            Exception("Generic exception")
        }
    }
}