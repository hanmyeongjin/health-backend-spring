package kr.co.fitpet.health.service.user

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.co.fitpet.health.dto.user.UserCash
import kr.co.fitpet.health.dto.user.UserDto
import kr.co.fitpet.health.exception.AuthorizationException
import kr.co.fitpet.health.repository.user.UserRedisRepository
import kr.co.fitpet.health.service.feign.UserFeignService
import java.util.*

internal class UserServiceTest : DescribeSpec({

    val token = "GMH3VA5YY8FK1AFMF4J6FQ9PK2P67KBS0A9DR5VXAQAM5K2BC7DF0EHRXG12HE2M"
    val userDto = UserDto(
        id = 1,
        name = "testUser",
        email = "email@fitpet.co.kr",
        mobileNumber = "01012345678",
        birthday = "19801231",
        userStatus = "ACTIVE",
        mileageId = 1,
        displayPetType = "D"
    )

    val userRedisRepository = mockk<UserRedisRepository>()
    val userFeignService = mockk<UserFeignService>()
    val userService = UserService(userRedisRepository, userFeignService)

    describe("user cash test") {
        context("user cash") {
            it("cash 조회") {
                every { userRedisRepository.findById(token) } answers { Optional.of(UserCash(token, userDto)) }
                val findUser = userService.getUser(token)

                findUser shouldBe userDto
            }
            it("API 조회") {
                every { userRedisRepository.findById(token) } answers { Optional.empty() }
                every { userFeignService.getUserInfoUseToken(token) } answers { userDto }
                val findUser = userService.getUser(token)

                findUser shouldBe userDto
            }
            it("없는 사용자 조회") {
                every { userRedisRepository.findById(any()) } answers { Optional.empty() }
                every { userFeignService.getUserInfoUseToken(any()) } answers { null }

                shouldThrow<AuthorizationException> {
                    userService.getUser(token)
                }
            }
        }

    }

})
