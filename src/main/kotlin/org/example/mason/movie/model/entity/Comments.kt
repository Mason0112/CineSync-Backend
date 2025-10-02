package org.example.mason.movie.model.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "comments",
    indexes = [
        Index(name = "idx_user_id", columnList = "user_id"),
        Index(name = "idx_movie_id", columnList = "movieId"),
        Index(name = "idx_created_at", columnList = "createdAt")
    ]
)
data class Comments(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var movieId: String,

    @Column(name = "user_id", nullable = false)
    var userId: Long,

    @Column(nullable = false, columnDefinition = "TEXT")
    var content: String,

    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime? =  null,

    var updatedAt: LocalDateTime? = null,
)