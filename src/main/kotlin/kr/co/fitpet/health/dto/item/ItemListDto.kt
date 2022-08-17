package kr.co.fitpet.health.dto.item

data class ItemListDto(
    val count: Int,
    val next: String,
    val previous: String,
    val result: List<ItemResponse> = mutableListOf()
)