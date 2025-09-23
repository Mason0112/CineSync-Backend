package org.example.mason.movie.model.dto

import org.example.mason.movie.model.enum.Role

data class UserRegAndLoginDto(
    val email: String,
    val userName: String,
    val password: String
)
data class LoginResponseDto(
    val token: String?,
    val user: UsersDto
)

data class UsersDto(
    val id: Long,
    val userName: String,
    val email: String,
    val role: Role
)