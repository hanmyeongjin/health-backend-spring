package kr.co.fitpet.health.dto.item

import io.swagger.v3.oas.annotations.media.Schema
import kr.co.fitpet.health.entity.item.Item

@Schema(description = "ItemSaveRequest")
data class ItemSaveRequest(
    
    @Schema(description = "아이템명")
    val name: String,

    @Schema(description = "아이템키")
    val key: String,

    @Schema(description = "반복타입")
    val cycleType: String,

    @Schema(description = "아이콘 URL")
    val iconUrl: String,

    @Schema(description = "아이템 타이틀")
    val title: String,

    @Schema(description = "아이템 타이틀2")
    val title2: String,

    @Schema(description = "아이템 포인트")
    val point: Int,

    @Schema(description = "아이템 MAX 포인트")
    val maxPoint: Int,

    @Schema(description = "open 여부")
    val isOpened: Int,

    @Schema(description = "position")
    val position: Int,

    @Schema(description = "meno")
    val memo: String
) {

    fun convertItem(): Item {
        return Item(
            name = name,
            key = key,
            cycleType = cycleType,
            iconUrl = iconUrl,
            title = title,
            title2 = title2,
            point = point,
            maxPoint = maxPoint,
            isOpened = isOpened,
            position = position,
            memo = memo,
        )
    }
}