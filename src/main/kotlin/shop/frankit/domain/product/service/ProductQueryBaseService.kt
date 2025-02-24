package shop.frankit.domain.product.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import shop.frankit.domain.product.exception.NotFoundProductException
import shop.frankit.domain.product.exception.NotFoundProductOptionException
import shop.frankit.domain.product.exception.NotProductOwnerException
import shop.frankit.domain.product.model.Product
import shop.frankit.domain.product.model.option.AbstractProductOption
import shop.frankit.domain.product.repository.ProductJpaRepository
import shop.frankit.domain.product.repository.ProductOptionJpaRepository

@Service
@Transactional(readOnly = true)
class ProductQueryBaseService(
    private val productOptionJpaRepository: ProductOptionJpaRepository,
    private val productJpaRepository: ProductJpaRepository
){
    fun findProductByIdOrThrow(productId: Long): Product {
        return productJpaRepository.findByIdOrNull(productId)
            ?: throw NotFoundProductException(productId = productId)
    }

    fun verifyProductOwner(product: Product, memberId: Long): Product {
       if(product.owner.id != memberId) {
           throw NotProductOwnerException(productId = product.id!!, requestMemberId = memberId)
       }
       return product
   }

    fun findProductOptionByIdOrThrow(optionId: Long): AbstractProductOption {
        return productOptionJpaRepository.findByIdOrNull(optionId)
            ?: throw NotFoundProductOptionException(optionId = optionId)
    }

}