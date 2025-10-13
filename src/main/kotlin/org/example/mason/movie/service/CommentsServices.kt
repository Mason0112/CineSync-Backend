package org.example.mason.movie.service

import org.example.mason.movie.mapper.toCommentDto
import org.example.mason.movie.mapper.toEntity
import org.example.mason.movie.model.dto.CommentDto
import org.example.mason.movie.model.dto.CommentRequest
import org.example.mason.movie.repo.CommentsRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CommentsServices(
    private val commentsRepository: CommentsRepository
) {

    fun getMovieComment(
        movieId: String,
        page: Int = 0,
        pageSize: Int = 10
    ): Page<CommentDto> {
        val pageable = PageRequest.of(
            page,
            pageSize,
            Sort.by(Sort.Direction.DESC, "createdAt")
        )

        return commentsRepository.findCommentsWithUserName(movieId, pageable)
    }


    fun createComment(request: CommentRequest, userId: Long): CommentDto {
        if (userId <= 0) {
            throw RuntimeException("Invalid user ID")
        }
        val comment = request.toEntity(userId)
        comment.createdAt = LocalDateTime.now()
        val save = commentsRepository.save(comment)
        return save.toCommentDto()
    }
}