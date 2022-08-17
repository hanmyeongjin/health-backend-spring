package kr.co.fitpet.health.service.feign

import kr.co.fitpet.health.dto.user.UserDto
import kr.co.fitpet.health.exception.AuthorizationException
import org.springframework.stereotype.Service

@Service
class UserFeignServiceSupport(
    private val userFeignService: UserFeignService
) {

    /**
     * return Int userId
     * throw 401
     */
    fun getUserId(token: String): Int {
        val userDto: UserDto? = userFeignService.getUserInfoUseToken(token)

        return if (userDto?.id != 0) userDto!!.id else
            throw AuthorizationException("사용자 토큰 에러")
    }
}