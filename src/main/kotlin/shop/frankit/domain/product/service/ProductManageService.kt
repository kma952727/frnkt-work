package shop.frankit.domain.product.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import shop.frankit.domain.member.service.MemberQueryBaseService
import shop.frankit.domain.product.dto.FindPageProductResponseDto
import shop.frankit.domain.product.dto.FindProductByIdResponseDto
import shop.frankit.domain.product.dto.RegisterProductRequestDto
import shop.frankit.domain.product.dto.UpdateProductRequestDto
import shop.frankit.domain.product.model.Product
import shop.frankit.domain.product.repository.ProductJpaRepository
import java.time.LocalDateTime

@Service
class ProductManageService(
    private val productJpaRepository: ProductJpaRepository,
    private val productQueryBaseService: ProductQueryBaseService,
    private val memberQueryBaseService: MemberQueryBaseService
) {
    @Transactional(readOnly = true)
    fun findPageProduct(page: Pageable): Page<FindPageProductResponseDto> {
        return productJpaRepository.findAll(page).map(::FindPageProductResponseDto)
    }

    fun findProductById(productId: Long): FindProductByIdResponseDto {
        return productQueryBaseService
            .findProductByIdOrThrow(productId = productId)
            .let(::FindProductByIdResponseDto)
    }

    @Transactional
    fun registerProduct(requestDto: RegisterProductRequestDto, memberId: Long, now: LocalDateTime) {
        productJpaRepository.save(
            Product(
                name = requestDto.name,
                basePrice = requestDto.basePrice,
                description = requestDto.description,
                owner = memberQueryBaseService.findMemberByIdOrThrow(memberId = memberId)
            )
        )
    }

    @Transactional
    fun updateProduct(requestDto: UpdateProductRequestDto, memberId: Long, productId: Long, now: LocalDateTime) {
        productQueryBaseService
            .findProductByIdOrThrow(productId = productId)
            .update(
                newName = requestDto.name,
                newDescription = requestDto.description,
                newBasePrice = requestDto.basePrice,
                memberId = memberQueryBaseService.findMemberByIdOrThrow(memberId = memberId).id!!,
                now = now
            )
    }

    @Transactional
    fun deleteProduct(memberId: Long, productId: Long, now: LocalDateTime) {
        productQueryBaseService.findProductByIdOrThrow(productId = productId)
            .softDeleteProduct(
                memberId = memberQueryBaseService.findMemberByIdOrThrow(memberId = memberId).id!!,
                now = now
            )
    }

}