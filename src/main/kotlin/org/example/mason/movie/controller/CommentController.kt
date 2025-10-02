package org.example.mason.movie.controller

import org.example.mason.movie.model.dto.CommentDto
import org.example.mason.movie.model.dto.CommentRequest
import org.example.mason.movie.security.UsersPrincipal
import org.example.mason.movie.service.CommentsServices
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/comments")
class CommentController(private val commentsServices: CommentsServices) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createComment(
        @RequestBody request: CommentRequest,
        @AuthenticationPrincipal user: UsersPrincipal
    ): ResponseEntity<CommentDto> {
        val createdComment = commentsServices.createComment(request, user.id)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment)
    }

    @GetMapping("/movie/{movieId}")
    fun getMovieComments(
        @PathVariable("movieId") movieId: String,
        @RequestParam("page") page: Int,
        @RequestParam(name = "pageSize", defaultValue = "5") pageSize: Int
    ): ResponseEntity<Page<CommentDto>> {
        val commentsPage = commentsServices.getMovieComment(movieId, page, pageSize)
        return ResponseEntity.ok(commentsPage)
    }

}