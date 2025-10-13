package org.example.mason.movie.repo

import org.example.mason.movie.model.dto.CommentDto
import org.example.mason.movie.model.entity.Comments
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query


@Repository
interface CommentsRepository : JpaRepository<Comments, Long>, JpaSpecificationExecutor<Comments> {

    fun findByUserId(userId: Long, pageable: Pageable): Page<Comments>
    fun countByUserId(userId: Long): Long

    @Query(
        """
    SELECT new org.example.mason.movie.model.dto.CommentDto(
        c.id, c.movieId, c.userId, u.userName, c.content, c.createdAt, c.updatedAt
    )
    FROM Comments c
    LEFT JOIN Users u ON c.userId = u.id
    WHERE c.movieId = :movieId
""",
        countQuery = """
    SELECT COUNT(c)
    FROM Comments c
    WHERE c.movieId = :movieId
"""
    )
    fun findCommentsWithUserName(movieId: String, pageable: Pageable): Page<CommentDto>


}
