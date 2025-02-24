package shop.frankit.domain.product.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.PositiveOrZero
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import shop.frankit.common.dto.FrankitResponseDto
import shop.frankit.common.dto.WebMemberDto
import shop.frankit.domain.product.dto.FindPageProductResponseDto
import shop.frankit.domain.product.dto.FindProductByIdResponseDto
import shop.frankit.domain.product.dto.RegisterProductRequestDto
import shop.frankit.domain.product.dto.UpdateProductRequestDto
import shop.frankit.domain.product.service.ProductManageService
import java.time.LocalDateTime

@Tag(name = "상품 관리")
@RestController
@RequestMapping("/api")
class ProductManageController(
    private val productManageService: ProductManageService
) {

    @Operation(summary = "id를 사용해 상품을 조회")
    @GetMapping("/products/{productId}")
    fun findProductById(
        @Schema(description = "조회하고 싶은 상품의 id")
        @PathVariable
        productId: Long
    ): ResponseEntity<FrankitResponseDto<FindProductByIdResponseDto>> {
        val result = productManageService.findProductById(productId = productId)

        return ResponseEntity
            .status(OK)
            .body(FrankitResponseDto(data = result))
    }

    @Operation(summary = "상품 목록을 조회(페이징)")
    @GetMapping("/products/page")
    fun findProductPage(
        @Schema(description = "페이지 번호 (첫 페이지 = 0, 음수 불가)")
        @RequestParam
        pageNumber: Int,

        @Schema(description = "페이지당 노출시키는 컨텐츠 사이즈 (음수 불가)")
        @RequestParam
        pageSize: Int
    ): ResponseEntity<FrankitResponseDto<Page<FindPageProductResponseDto>>> {
        val result = productManageService.findPageProduct(PageRequest.of(pageNumber, pageSize))

        return ResponseEntity.ok(FrankitResponseDto(data = result))
    }

    @Operation(summary = "새로운 상품 등록")
    @PostMapping("/products")
    fun registerProduct(
        @Valid
        @RequestBody
        requestDto: RegisterProductRequestDto,

        @Schema(hidden = true)
        webMemberDto: WebMemberDto
    ): ResponseEntity<FrankitResponseDto<Nothing>> {
        productManageService.registerProduct(
            requestDto = requestDto,
            memberId = webMemberDto.memberId,
            now = LocalDateTime.now()
        )

        return ResponseEntity.status(CREATED).body(FrankitResponseDto())
    }

    @Operation(summary = "상품을 수정")
    @PutMapping("/products/{productId}")
    fun updateProduct(
        @Valid
        @RequestBody
        requestDto: UpdateProductRequestDto,

        @Schema(description = "수정하고 싶은 상품의 id")
        @PathVariable
        productId: Long,

        @Schema(hidden = true)
        webMemberDto: WebMemberDto
    ): ResponseEntity<FrankitResponseDto<Nothing>> {
        productManageService.updateProduct(
            requestDto = requestDto,
            memberId = webMemberDto.memberId,
            productId = productId,
            now = LocalDateTime.now()
        )

        return ResponseEntity.status(NO_CONTENT).body(FrankitResponseDto())
    }

    @Operation(summary = "상품 삭제")
    @DeleteMapping("/products/{productId}")
    fun deleteProduct(
        @Schema(name = "삭제를 원하는 상품 id")
        @PathVariable
        productId: Long,

        @Schema(hidden = true)
        webMemberDto: WebMemberDto
    ): ResponseEntity<FrankitResponseDto<Nothing>> {
        productManageService.deleteProduct(
            memberId = webMemberDto.memberId,
            productId = productId,
            now = LocalDateTime.now()
        )

        return ResponseEntity.status(NO_CONTENT).body(FrankitResponseDto())
    }
}