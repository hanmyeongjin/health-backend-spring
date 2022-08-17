package kr.co.fitpet.health.repository.user

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.co.fitpet.health.dto.user.UserCash
import kr.co.fitpet.health.dto.user.UserDto
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
internal class UserRedisRepositoryTest: DescribeSpec() {

    private val log = KotlinLogging.logger {}

    override fun extensions() = listOf(SpringExtension)

    @Autowired(required = false)
    private lateinit var userRedisRepository: UserRedisRepository

    init {
        this.describe("user redis test") {
            context("redis save_정상") {
                val saveUserCash = userRedisRepository.save(userCash2)

                log.info { "saveUserCash: $saveUserCash" }

                saveUserCash shouldNotBe null
                saveUserCash shouldBe userCash2
            }
            context("redis findAll_정상") {
                val findAll = userRedisRepository.findAll()
                log.info { "findAll: $findAll" }
                findAll shouldNotBe null
            }
            context("redis get_정상") {
                val findUserCash = userRedisRepository.findById(userCash2.userToken!!)
                log.info { "findUserCash: $findUserCash" }

                findUserCash shouldNotBe null
                findUserCash.isEmpty shouldNotBe true
                findUserCash.get() shouldBe userCash2
            }

        }
    }

    companion object {
        val userCash2 = UserCash(
            userToken = "1",
            userDto = UserDto(
                id = 1,
                name = "testName1",
                email = "test@fitpet.co.kr",
                mobileNumber = "01012345678",
                birthday = "20001231",
                userStatus = "ACTIVE",
                mileageId = 1,
                displayPetType = "D"
            )
        )

    }
}