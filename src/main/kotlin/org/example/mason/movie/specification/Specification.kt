package org.example.mason.movie.specification

import jakarta.persistence.criteria.Predicate
import org.example.mason.movie.model.entity.Comments
import org.springframework.data.jpa.domain.Specification
import java.time.LocalDateTime

object CommentsSpecification {

    fun hasUserId(userId: Long?): Specification<Comments> {
        return Specification { root, _, criteriaBuilder ->
            userId?.let {
                criteriaBuilder.equal(root.get<Long>("userId"), it)
            }
        }
    }

    fun hasMovieId(movieId: String?): Specification<Comments> {
        return Specification { root, _, criteriaBuilder ->
            movieId?.let {
                criteriaBuilder.equal(root.get<String>("movieId"), it)
            }
        }
    }

    fun contentContains(keyword: String?): Specification<Comments> {
        return Specification { root, _, criteriaBuilder ->
            keyword?.let {
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("content")),
                    "%${it.lowercase()}%"
                )
            }
        }
    }

    fun createdAfter(date: LocalDateTime?): Specification<Comments> {
        return Specification { root, _, criteriaBuilder ->
            date?.let {
                criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), it)
            }
        }
    }

    fun createdBefore(date: LocalDateTime?): Specification<Comments> {
        return Specification { root, _, criteriaBuilder ->
            date?.let {
                criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), it)
            }
        }
    }
    // 組合條件的便捷方法
    // ✅ 新版本的組合方式
    fun buildSpecification(
        userId: Long? = null,
        movieId: String? = null,
        keyword: String? = null,
        startDate: LocalDateTime? = null,
        endDate: LocalDateTime? = null
    ): Specification<Comments> {
        return Specification { root, query, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()

            userId?.let {
                predicates.add(criteriaBuilder.equal(root.get<Long>("userId"), it))
            }

            movieId?.let {
                predicates.add(criteriaBuilder.equal(root.get<String>("movieId"), it))
            }

            keyword?.let {
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("content")),
                        "%${it.lowercase()}%"
                    )
                )
            }

            startDate?.let {
                predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), it)
                )
            }

            endDate?.let {
                predicates.add(
                    criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), it)
                )
            }
            if (predicates.isEmpty()) {
                null
            } else {
                criteriaBuilder.and(*predicates.toTypedArray())
            }
        }
    }

    // ✅ 使用 allOf (Spring Data JPA 3.5+)
    fun buildSpecificationWithAllOf(
        userId: Long? = null,
        movieId: String? = null,
        keyword: String? = null,
        startDate: LocalDateTime? = null,
        endDate: LocalDateTime? = null
    ): Specification<Comments> {
        val specs = mutableListOf<Specification<Comments>>()

        hasUserId(userId)?.let { specs.add(it) }
        hasMovieId(movieId)?.let { specs.add(it) }
        contentContains(keyword)?.let { specs.add(it) }
        createdAfter(startDate)?.let { specs.add(it) }
        createdBefore(endDate)?.let { specs.add(it) }
        return if (specs.isEmpty()) {
            Specification { _, _, _ -> null }
        } else {
            Specification.allOf(specs)
        }
    }


}
