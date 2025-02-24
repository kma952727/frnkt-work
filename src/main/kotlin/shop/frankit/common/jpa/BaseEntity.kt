package shop.frankit.common.jpa

import jakarta.persistence.*
import jakarta.persistence.GenerationType.IDENTITY
import org.hibernate.annotations.Comment
import org.hibernate.annotations.SQLRestriction
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
@SQLRestriction("is_deleted is null")
abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Comment("pk")
    @Column(name = "id")
    val id: Long? = null

    @Comment("논리적 삭제 여부")
    @Column(name = "is_deleted")
    var isDeleted: Boolean = false

    @Comment("데이터 논리적 삭제 날짜")
    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime? = null

    @Comment("데이터 생성 날짜")
    @Column(name = "created_at")
    val createdAt: LocalDateTime? = LocalDateTime.now()

    @Comment("데이터 생성 날짜")
    @LastModifiedDate
    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null

    fun softDeleteBaseEntity(now: LocalDateTime) {
        isDeleted = true
        deletedAt = now
    }

    @PreUpdate
    fun preUpdate() {
        updatedAt = LocalDateTime.now()
    }

}