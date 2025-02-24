package shop.frankit.domain.product.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.PositiveOrZero

data class UpdateProductRequestDto (

    @field:NotBlank(message = "name : 공백만 포함 불가")
    @Schema(description = "수정 결과 이름 (공백만 포함 불가")
    val name: String,

    @field:NotBlank(message = "description : 공백만 포함 불가")
    @Schema(description = "수정 결과 상품 설명 (공백만 포함 불가)")
    val description: String,

    @field:PositiveOrZero(message = "basePrice : 음수 불가")
    @Schema(description = "수정 결과 가격 (음수 불가)")
    val basePrice: Int
)