package shop.frankit.helper

import shop.frankit.common.jpa.BaseEntity

class EntityIdInjector {
    companion object {
        fun <T : BaseEntity> inject(entity: T, id: Long) {
            val field = BaseEntity::class.java.getDeclaredField("id")
            field.isAccessible = true
            field.set(entity, id)
        }
    }
}