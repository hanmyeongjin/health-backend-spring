package kr.co.fitpet.health.service.user

import kr.co.fitpet.health.dto.user.UserCash
import kr.co.fitpet.health.dto.user.UserDto
import kr.co.fitpet.health.exception.AuthorizationException
import kr.co.fitpet.health.repository.user.UserRedisRepository
import kr.co.fitpet.health.service.feign.UserFeignService
import mu.KotlinLogging
import java.util.*

class UserService(
    private val userRedisRepository: UserRedisRepository,
    private val userFeignService: UserFeignService,
) {

    private val log = KotlinLogging.logger {}

    fun getUser(userToken: String): UserDto {
        var findUser = userRedisRepository.findById(userToken) // 1차 cash

        log.info { "findUserCash: $findUser" }

        if(findUser.isEmpty) {
            findUser = Optional.of(UserCash(userToken, userFeignService.getUserInfoUseToken(userToken))) // 2차 API 통신
        }

        log.info { "findUser: $findUser" }

        if(findUser.isEmpty || findUser.get().userDto == null) {
            throw AuthorizationException()
        }

        userRedisRepository.save(findUser.get())

        return findUser.get().userDto!!
    }
}

