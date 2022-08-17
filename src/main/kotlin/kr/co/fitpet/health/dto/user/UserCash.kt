package kr.co.fitpet.health.dto.user

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash("UserCash")
data class UserCash(

    @Id
    val userToken: String? = null,

    val userDto: UserDto? = null
)