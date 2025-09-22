package org.example.mason.movie.model.dto

import org.example.mason.movie.model.enum.Role

data class UserRegAndLoginDto(
    val email: String,
    val password: String
)
data class LoginResponseDto(
    val token: String?,
    val user: UserDto
)

data class UserDto(
    val id: Long,
    val userName: String,
    val email: String,
    val role: Role
)