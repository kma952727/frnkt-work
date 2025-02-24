package shop.frankit.domain.product.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.PositiveOrZero
import jakarta.validation.constraints.Size

data class UpdateProductOptionRequestDto(
    @Schema(description = "옵션 이름 (공백만 포함 불가)")
    @field:NotBlank(message = "optionName : 공백만 포함할 수 없음")
    val optionName: String,

    @field:Size(min = 1 ,message = "updateOptionAttributeList : 최소 1개 필요")
    @field:Valid
    @Schema(description = "옵션에 포함된 선택지 목록 (최소 1개 필요)")
    val updateOptionAttributeList: List<ProductOptionAttributeResponseDto>
) {

    data class ProductOptionAttributeResponseDto(
        @Schema(description = "선택지 id")
        val attributeId: Long,

        @Schema(description = "선택지 이름 (공백만 포함 불가)")
        @field:NotBlank(message = "updateOptionAttributeList.attributeName : 공백만 포함할 수 없음")
        val attributeName: String,

        @Schema(description = "선택지 가격 (음수 불가)")
        @field:PositiveOrZero(message = "updateOptionAttributeList.attributePrice : 음수 불가")
        val attributePrice: Int
    )
}