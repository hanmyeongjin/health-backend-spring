package kr.co.fitpet.health.service.feign

import kr.co.fitpet.health.config.UserFeignClientConfiguration
import kr.co.fitpet.health.dto.user.UserDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "fitpet-user-service", url = "\${fitpet.gateway.fitpet-user}", configuration = [UserFeignClientConfiguration::class])
interface UserFeignService {

    @GetMapping("/get/{token}")
    fun getUserInfoUseToken(@PathVariable("token") token: String): UserDto?

}