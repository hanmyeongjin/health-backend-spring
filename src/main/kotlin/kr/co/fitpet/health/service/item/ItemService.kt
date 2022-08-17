package kr.co.fitpet.health.service.item

import kr.co.fitpet.health.dto.item.ItemListDto
import kr.co.fitpet.health.dto.item.ItemResponse
import kr.co.fitpet.health.dto.item.ItemSaveRequest
import kr.co.fitpet.health.exception.BadRequestException
import kr.co.fitpet.health.repository.item.ItemRepository
import kr.co.fitpet.health.repository.item.ItemRepositorySupport
import kr.co.fitpet.health.service.feign.UserFeignServiceSupport
import mu.KotlinLogging
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ItemService(
    private val itemRepository: ItemRepository,
    private val itemRepositorySupport: ItemRepositorySupport,
    private val userService: UserFeignServiceSupport
) {
    val log = KotlinLogging.logger {}

    @Transactional(readOnly = true)
    fun getItemList(token: String, pageable: Pageable): ItemListDto {
        val userId = userService.getUserId(token)
        log.info { "userId: $userId" }

        val findItem = itemRepositorySupport.getOpenItemList(pageable)
        log.info { "getItemList : $findItem" }
        return ItemListDto(
            count = findItem.totalPages,
            next = "",
            previous = "",
            result = findItem.content.map {
                ItemResponse.getItemResponse(it)
            }
        )
    }

    @Transactional(readOnly = true)
    fun getItem(token: String, itemNo: Int): ItemResponse {
        val userId = userService.getUserId(token)
        log.info { "userId: $userId" }

        val findItem = itemRepository.findById(itemNo)
        log.info { "get Item[$itemNo] : $findItem" }
        when(findItem.isEmpty) {
            true -> throw BadRequestException("해당 아이템을 찾을 수 없습니다.")
            else -> return ItemResponse.getItemResponse(findItem.get())
        }
    }

    @Transactional(readOnly = false)
    fun saveItem(token: String, itemSaveRequest: ItemSaveRequest): ItemResponse {
        val userId = userService.getUserId(token)
        log.info { "userId: $userId" }
        val savedItem = itemRepository.save(itemSaveRequest.convertItem())
        log.info { "saveItem : $savedItem" }
        return ItemResponse.getItemResponse(savedItem)
    }

    @Transactional(readOnly = false)
    fun modifyItem(token: String, itemNo: Int, itemSaveRequest: ItemSaveRequest): ItemResponse {
        val userId = userService.getUserId(token)
        log.info { "userId: $userId" }
        val findItem = itemRepository.findById(itemNo).orElseThrow {  BadRequestException("해당 아이템을 찾을 수 없습니다.") }

        var saveItem = itemSaveRequest.convertItem()
        saveItem.id = findItem.id

        val savedItem = itemRepository.save(saveItem)
        log.info { "saveItem : $savedItem" }
        return ItemResponse.getItemResponse(savedItem)
    }
}