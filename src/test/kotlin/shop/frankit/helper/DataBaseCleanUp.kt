package shop.frankit.helper

import jakarta.persistence.EntityManager
import jakarta.persistence.Table
import jakarta.transaction.Transactional
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component

@Component
class DataBaseCleanUp(
    private val entityManager: EntityManager
) : InitializingBean {
    private lateinit var tableNames: List<String>

    override fun afterPropertiesSet() {
        // Table Annotation 활용하여 테이블 명 구하기
		tableNames = entityManager.metamodel.entities
			.filter { it.javaType.getAnnotation(Table::class.java) != null }
            .map { it.javaType.getAnnotation(Table::class.java)?.name!! }
	}

    /**
     * Table 어노테이션이 할당된 모든 엔티티의 데이터를 초기화
     * 1. truncate
     * 2. primary key 초기화
     */
    @Transactional
    fun cleanRdb() {
        entityManager.flush()
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE;").executeUpdate()
        for (tableName in tableNames) {
            entityManager
                .createNativeQuery(
                    """
                        TRUNCATE TABLE $tableName;
                        ALTER TABLE $tableName ALTER COLUMN id RESTART WITH 1;
                    """.trimIndent()
                )
                .executeUpdate()

        }
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE;").executeUpdate()
    }
}