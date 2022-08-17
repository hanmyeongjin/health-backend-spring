package kr.co.fitpet.health.service.item

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.co.fitpet.health.dto.item.ItemResponse
import kr.co.fitpet.health.dto.item.ItemSaveRequest
import kr.co.fitpet.health.entity.item.Item
import kr.co.fitpet.health.exception.AuthorizationException
import kr.co.fitpet.health.exception.BadRequestException
import kr.co.fitpet.health.repository.item.ItemRepository
import kr.co.fitpet.health.repository.item.ItemRepositorySupport
import kr.co.fitpet.health.service.feign.UserFeignServiceSupport
import kr.co.fitpet.health.service.item.ItemService
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.*

internal class ItemServiceTest : DescribeSpec({

    val itemRepository = mockk<ItemRepository>()

    val userService = mockk<UserFeignServiceSupport>()

    val itemRepositorySupport = mockk<ItemRepositorySupport>()

    val itemService = ItemService(itemRepository, itemRepositorySupport, userService)

    val token = "GMH3VA5YY8FK1AFMF4J6FQ9PK2P67KBS0A9DR5VXAQAM5K2BC7DF0EHRXG12HE2M"

    var mockItem1 = Item(
        id = 1,
        name = "mockTestItem1",
        key = "key1",
        cycleType = "MONTHLY",
        iconUrl = "https://testurl.com/testicon.png",
        title = "test tile1",
        title2 = "test title2",
        point = 100,
        maxPoint = 1000,
        isOpened = 1,
        position = 1,
        memo = "memo1",
    )

    var mockItem2 = Item(
        id = 2,
        name = "mockTestItem2",
        key = "key2",
        cycleType = "WEEKLY",
        iconUrl = "https://testurl.com/testicon.png",
        title = "test tile2",
        title2 = "test title3",
        point = 10,
        maxPoint = 100,
        isOpened = 0,
        position = 2,
        memo = "memo2",
    )

    beforeEach {
        every { userService.getUserId(any()) } answers { 1 }
    }

    describe("itemService") {
        context("item find all") {
            it("모든 아이템 리턴") {

                var itemListMock = listOf(mockItem1, mockItem2)
                every { itemRepositorySupport.getOpenItemList(any()) } answers {
                    PageImpl(itemListMock, PageRequest.of(0, 1),itemListMock.size.toLong())
                }

                val itemList = itemService.getItemList(token, PageRequest.of(0, 10))

                itemList.count shouldBe itemListMock.size
                itemList.result shouldBe itemListMock.map { it -> ItemResponse.getItemResponse(it) }
            }
            it("빈 아이템 리스트 리턴") {
                val pageItem = PageImpl(mutableListOf<Item>(), PageRequest.of(0,10), 0L)
                every { itemRepositorySupport.getOpenItemList(any()) }  returns pageItem

                val itemList = itemService.getItemList(token, PageRequest.of(0, 10))

                itemList.count shouldBe 0
                itemList.result shouldBe mutableListOf<Item>()
            }
            it("토큰 오류") {
                every { userService.getUserId(any()) } throws AuthorizationException()
                every { itemRepository.findAll() } returns listOf()

                shouldThrow<AuthorizationException> {
                    itemService.getItemList(token, PageRequest.of(0, 10))
                }
            }
        }

        context("item find") {
            it("정상") {
                var itemNo = mockItem1.id!!
                every { itemRepository.findById(itemNo) } returns Optional.of(mockItem1)

                val findItem = itemService.getItem(token, itemNo)
                findItem shouldBe ItemResponse.getItemResponse(mockItem1)
            }

            it("없는 아이템 NO") {

                every { itemRepository.findById(any()) } returns Optional.empty()

                shouldThrow<BadRequestException> {
                    itemService.getItem(token, 1)
                }
            }
        }

        context("save item") {
            it("정상") {
                var itemSaveRequest = mockk<ItemSaveRequest>()
                every { itemSaveRequest.convertItem() } returns mockItem1
                every { itemRepository.save(any()) } returns mockItem1

                val savedItem = itemService.saveItem(token, itemSaveRequest)

                savedItem shouldBe ItemResponse.getItemResponse(mockItem1)
            }
        }

        context("modify item") {
            var itemSaveRequest = mockk<ItemSaveRequest>()
            val copyItem = mockItem1.copy(
                name = "copy Item1",
                key = "copy key1",
                cycleType = "MONTHLY",
                iconUrl = "https://testurl.com/copy-testicon.png",
                title = "copy tile1",
                title2 = "copy title2",
                point = 99,
                maxPoint = 999,
                isOpened = 0,
                position = 9,
                memo = "copy memo1",
            )

            it("정상") {
                every { itemSaveRequest.convertItem() } returns copyItem
                every { itemRepository.findById(mockItem1.id!!) } returns Optional.of(mockItem1)
                every { itemRepository.save(any()) } returns copyItem

                val modifyItem = itemService.modifyItem(token, mockItem1.id!!, itemSaveRequest)

                modifyItem shouldBe ItemResponse.getItemResponse(copyItem)
            }

            it("없는 아이템 NO") {
                every { itemSaveRequest.convertItem() } returns copyItem
                every { itemRepository.findById(any()) } returns Optional.empty()
                every { itemRepository.save(any()) } returns copyItem

                shouldThrow<BadRequestException> {
                    itemService.modifyItem(token, 1, itemSaveRequest)
                }
            }
        }
    }
})
