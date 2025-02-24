package shop.frankit.domain.product.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import shop.frankit.domain.product.dto.service.AddOptionDto
import shop.frankit.domain.product.dto.FindProductOptionByIdResponseDto
import shop.frankit.domain.product.dto.AddProductOptionRequestDto
import shop.frankit.domain.product.dto.UpdateProductOptionRequestDto
import shop.frankit.domain.product.dto.service.UpdateOptionDto
import shop.frankit.domain.product.exception.NotFoundProductException
import shop.frankit.domain.product.exception.NotMatchProductAndOptionException
import shop.frankit.domain.product.repository.ProductJpaRepository
import java.time.LocalDateTime

@Service
class ProductOptionManageService(
    private val productQueryBaseService: ProductQueryBaseService,
    private val productJpaRepository: ProductJpaRepository
) {
    @Transactional(readOnly = true)
    fun findOptionById(optionId: Long): FindProductOptionByIdResponseDto {
        return productQueryBaseService
            .findProductOptionByIdOrThrow(optionId = optionId)
            .let(::FindProductOptionByIdResponseDto)
    }

    @Transactional
    fun updateProductOption(
        memberId: Long,
        productId: Long,
        optionId: Long,
        requestDto: UpdateProductOptionRequestDto,
        now: LocalDateTime = LocalDateTime.now()
    ) {
        productJpaRepository.findByIdOrNull(id = productId)
            ?.let { product ->
                productQueryBaseService.verifyProductOwner(product = product, memberId = memberId)

                product.optionList
                    .find { it.id == optionId }
                    ?.update(updateOption = UpdateOptionDto(requestDto = requestDto), now = now)
                    ?: throw NotMatchProductAndOptionException(optionId = optionId)
            }
            ?: throw NotFoundProductException(productId = productId)
    }

    @Transactional
    fun deleteProductOption(productId: Long, optionId: Long, memberId: Long, now: LocalDateTime) {
        productQueryBaseService
            .findProductByIdOrThrow(productId = productId)
            .softDeleteOption(
                optionId = optionId,
                memberId = memberId,
                now = now
            )
    }

    @Transactional
    fun addProductOption(
        requestDto: AddProductOptionRequestDto,
        productId: Long,
        memberId: Long
    ) {
        val product = productQueryBaseService.verifyProductOwner(
            product = productQueryBaseService.findProductByIdOrThrow(productId = productId),
            memberId = memberId
        )

        product.addOption(addOptionDto = AddOptionDto(requestDto = requestDto))
    }

}