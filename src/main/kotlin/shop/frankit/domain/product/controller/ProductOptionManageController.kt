package shop.frankit.domain.product.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import shop.frankit.common.dto.FrankitResponseDto
import shop.frankit.common.dto.WebMemberDto
import shop.frankit.domain.product.dto.AddProductOptionRequestDto
import shop.frankit.domain.product.dto.FindProductOptionByIdResponseDto
import shop.frankit.domain.product.dto.UpdateProductOptionRequestDto
import shop.frankit.domain.product.service.ProductOptionManageService
import java.time.LocalDateTime

@Tag(name = "상품 옵션, 선택지 관리")
@Validated
@RestController
@RequestMapping("/api")
class ProductOptionManageController(
    private val productOptionManageService: ProductOptionManageService
) {
    @Operation(summary = "상품의 옵션 1개 조회")
    @GetMapping("/products/options/{optionId}")
    fun findOptionById(
        @Schema(description = "조회하고 싶은 옵션 id")
        @PathVariable
        optionId: Long
    ): ResponseEntity<FrankitResponseDto<FindProductOptionByIdResponseDto>> {
        val result = productOptionManageService.findOptionById(optionId = optionId)

        return ResponseEntity.ok(FrankitResponseDto(data = result))
    }

    @Operation(summary = "상품에 옵션 1개 추가")
    @PostMapping("/products/{productId}/options")
    fun addProductOption(
        @Schema(description = "옵션을 추가하고 싶은 상품 id")
        @PathVariable
        productId: Long,

        @Schema(description = "추가하고 싶은 옵션 데이터")
        @Valid
        @RequestBody
        requestDto: AddProductOptionRequestDto,

        @Schema(hidden = true)
        webMemberDto: WebMemberDto
    ): ResponseEntity<FrankitResponseDto<Nothing>> {
        productOptionManageService.addProductOption(
            requestDto = requestDto,
            productId = productId,
            memberId = webMemberDto.memberId
        )

        return ResponseEntity.status(CREATED).body(FrankitResponseDto())
    }

    @Operation(summary = "상품의 옵션 1개 수정")
    @PutMapping("/products/{productId}/options/{optionId}")
    fun updateProductOption(
        @Schema(description = "수정하고 싶은 옵션이 포함된 상품 id")
        @PathVariable
        productId: Long,

        @Schema(description = "수정하고 싶은 옵션 id")
        @PathVariable
        optionId: Long,

        @Valid
        @RequestBody
        requestDto: UpdateProductOptionRequestDto,

        @Schema(hidden = true)
        webMemberDto: WebMemberDto
    ): ResponseEntity<FrankitResponseDto<Nothing>> {
        productOptionManageService.updateProductOption(
            memberId = webMemberDto.memberId,
            productId = productId,
            optionId = optionId,
            requestDto = requestDto
        )

        return ResponseEntity.status(NO_CONTENT).body(FrankitResponseDto())
    }

    @Operation(summary = "상품의 옵션 1개 삭제")
    @DeleteMapping("/products/{productId}/options/{optionId}")
    fun deleteProductOption(
        @Schema(description = "삭제하고 싶은 옵션을 포함하는 상품 id")
        @PathVariable
        productId: Long,

        @Schema(description = "삭제하고 싶은 옵션 id")
        @PathVariable
        optionId: Long,

        @Schema(hidden = true)
        webMemberDto: WebMemberDto
    ): ResponseEntity<FrankitResponseDto<Nothing>> {
        productOptionManageService.deleteProductOption(
            productId = productId,
            optionId = optionId,
            memberId = webMemberDto.memberId,
            now = LocalDateTime.now()
        )

        return ResponseEntity.status(NO_CONTENT).body(FrankitResponseDto())
    }
}