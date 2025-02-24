package shop.frankit.domain.product.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.PositiveOrZero
import jakarta.validation.constraints.Size
import shop.frankit.domain.product.model.vo.ProductOptionType

data class AddProductOptionRequestDto (

    @Schema(description = "옵션 이름 (공백만 포함 불가)")
    @field:NotBlank(message = "optionName : 공백만 포함할 수 없음")
    val optionName: String,

    @Schema(description = "옵션 타입")
    val optionType: ProductOptionType,

    @Schema(description = "옵션에 포함되는 선택지 목록 (최소 1개 필요)")
    @field:Size(min = 1, message = "optionAttributeList : 최소 1개 필요")
    @field:Valid
    val optionAttributeList: List<ProductOptionAttributeResponseDto>
) {

    data class ProductOptionAttributeResponseDto (
        @Schema(description = "선택지 이름")
        @field:NotBlank(message = "optionAttributeList.attributeName: 필수 데이터")
        val attributeName: String,

        @field:PositiveOrZero(message = "optionAttributeList.price: 음수 불가")
        @Schema(description = "선택지 가격 (음수 불가)")
        val price: Int
    )

}