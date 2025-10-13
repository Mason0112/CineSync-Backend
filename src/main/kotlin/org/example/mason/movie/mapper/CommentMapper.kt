package org.example.mason.movie.mapper

import org.example.mason.movie.model.dto.CommentDto
import org.example.mason.movie.model.dto.CommentRequest
import org.example.mason.movie.model.entity.Comments

fun Comments.toCommentDto(): CommentDto {
    return CommentDto(
        id = this.id ?: throw IllegalStateException("Comment ID cannot be null"),
        movieId = this.movieId,
        userId = this.userId,
        content = this.content,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

fun CommentRequest.toEntity(userId: Long): Comments {
    return Comments(
        id = null,
        movieId = this.movieId,
        userId = userId,
        content = this.content,
        createdAt = null,
        updatedAt = null
    )

}