package kr.co.fitpet.health.controller.item

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import kr.co.fitpet.health.dto.item.ItemListDto
import kr.co.fitpet.health.dto.item.ItemResponse
import kr.co.fitpet.health.dto.item.ItemSaveRequest
import kr.co.fitpet.health.exception.BadRequestException
import kr.co.fitpet.health.service.item.ItemService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("/health/items")
class ItemController(
    private val itemService: ItemService
) {

    @Operation(
        summary = "전체 아이템 리스트 조회",
        description = "전체 아이템 리스트 조회",
        responses = [ApiResponse(
            responseCode = "200",
            description = "아이템 리스트 조회 성공",
            content = arrayOf(
                Content(
                    mediaType = "application/json",
                    array = ArraySchema(schema = Schema(implementation = ItemListDto::class))
                )
            )
        ), ApiResponse(
            responseCode = "400",
            description = "잘못된 요청입니다. (validation error)",
            content = arrayOf()
        ), ApiResponse(
            responseCode = "401",
            description = "사용자 인증 실패",
            content = arrayOf()
        ), ApiResponse(
            responseCode = "4xx, 5xx",
            description = "에러 정보 응답",
            content = arrayOf(Content(mediaType = "application/json", schema = Schema(implementation = BadRequestException::class)))
        )]
    )
    @GetMapping("/")
    fun getItemList(@RequestHeader("Authorization") userToken: String, @RequestParam page: Int, @RequestParam page_size: Int): ItemListDto {
        val pageable: Pageable = PageRequest.of(page, page_size)
        return itemService.getItemList(userToken, pageable)
    }

    @Operation(
        summary = "아이템 조회",
        description = "아이템 조회",
        responses = [ApiResponse(
            responseCode = "200",
            description = "아이템 조회 성공",
            content = arrayOf(
                Content(
                    mediaType = "application/json",
                    array = ArraySchema(schema = Schema(implementation = ItemResponse::class))
                )
            )
        ), ApiResponse(
            responseCode = "400",
            description = "잘못된 요청입니다. (validation error)",
            content = arrayOf()
        ), ApiResponse(
            responseCode = "401",
            description = "사용자 인증 실패",
            content = arrayOf()
        ), ApiResponse(
            responseCode = "4xx, 5xx",
            description = "에러 정보 응답",
            content = arrayOf(Content(mediaType = "application/json", schema = Schema(implementation = BadRequestException::class)))
        )]
    )
    @GetMapping("/{itemNo}")
    fun getItem(@RequestHeader("Authorization") userToken: String,
                @PathVariable @NotNull(message = "itemNo는 필수입니다.") itemNo: Int): ResponseEntity<ItemResponse> {
        return ResponseEntity.ok(itemService.getItem(userToken, itemNo))
    }

    @Operation(
        summary = "아이템 생성",
        description = "아이템 생성",
        responses = [ApiResponse(
            responseCode = "201",
            description = "아이템 생성 성공",
            content = arrayOf(
                Content(
                    mediaType = "application/json",
                    array = ArraySchema(schema = Schema(implementation = ItemResponse::class))
                )
            )
        ), ApiResponse(
            responseCode = "400",
            description = "잘못된 요청입니다. (validation error)",
            content = arrayOf()
        ), ApiResponse(
            responseCode = "401",
            description = "사용자 인증 실패",
            content = arrayOf()
        ), ApiResponse(
            responseCode = "4xx, 5xx",
            description = "에러 정보 응답",
            content = arrayOf(Content(mediaType = "application/json", schema = Schema(implementation = BadRequestException::class)))
        )]
    )
    @PostMapping("/")
    fun saveItem(@RequestHeader("Authorization") userToken: String,
                 @RequestBody itemSaveRequest: ItemSaveRequest): ResponseEntity<ItemResponse> {
        return ResponseEntity<ItemResponse>(itemService.saveItem(userToken, itemSaveRequest), HttpStatus.CREATED)
    }

    @Operation(
        summary = "아이템 수정",
        description = "아이템 수정",
        responses = [ApiResponse(
            responseCode = "201",
            description = "아이템 수정 성공",
            content = arrayOf(
                Content(
                    mediaType = "application/json",
                    array = ArraySchema(schema = Schema(implementation = ItemResponse::class))
                )
            )
        ), ApiResponse(
            responseCode = "400",
            description = "잘못된 요청입니다. (validation error)",
            content = arrayOf()
        ), ApiResponse(
            responseCode = "401",
            description = "사용자 인증 실패",
            content = arrayOf()
        ), ApiResponse(
            responseCode = "4xx, 5xx",
            description = "에러 정보 응답",
            content = arrayOf(Content(mediaType = "application/json", schema = Schema(implementation = BadRequestException::class)))
        )]
    )
    @PatchMapping("/{itemNo}")
    fun modifyItem(@RequestHeader("Authorization") userToken: String,
                   @PathVariable itemNo: Int,
                   @RequestBody itemSaveRequest: ItemSaveRequest): ResponseEntity<ItemResponse> {
        return ResponseEntity<ItemResponse>(itemService.modifyItem(userToken, itemNo, itemSaveRequest), HttpStatus.CREATED)
    }
}