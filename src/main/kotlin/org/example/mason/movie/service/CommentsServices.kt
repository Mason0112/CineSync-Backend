package org.example.mason.movie.service

import org.example.mason.movie.mapper.toCommentDto
import org.example.mason.movie.mapper.toEntity
import org.example.mason.movie.model.dto.CommentDto
import org.example.mason.movie.model.dto.CommentRequest
import org.example.mason.movie.repo.CommentsRepository
import org.example.mason.movie.specification.CommentsSpecification
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class CommentsServices(
    private val commentsRepository: CommentsRepository
) {

    fun getMovieComment(
        movieId: String,
        page: Int = 0,
        pageSize: Int = 10
    ): Page<CommentDto> {
        val specification = CommentsSpecification.buildSpecification(
            movieId = movieId
        )
        val pageable = PageRequest.of(
            page,
            pageSize,
            Sort.by(Sort.Direction.DESC, "createdAt")
        )

        val commentResults = commentsRepository.findAll(specification, pageable)
        return commentResults.map { it.toCommentDto() }
    }

    fun createComment(request: CommentRequest, userId: Long): CommentDto {
        if (userId <= 0) {
            throw RuntimeException("Invalid user ID")
        }

        return commentsRepository
            .save(request.toEntity(userId))
            .toCommentDto()
    }
}