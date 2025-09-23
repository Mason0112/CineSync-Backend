package org.example.mason.movie.mapper

import org.example.mason.movie.model.dto.UsersDto
import org.example.mason.movie.model.dto.UserRegAndLoginDto
import org.example.mason.movie.model.entity.Users

fun Users.toDto(): UsersDto {
    return UsersDto(
        id = this.id ?: throw IllegalStateException("User ID cannot be null for DTO conversion"),
        userName = this.userName,
        email = this.email,
        role = this.role
    )
}

fun UserRegAndLoginDto.toEntity(): Users {
    return Users(
        userName = this.email, // Assuming email is used as userName for registration
        email = this.email,
        password = this.password,
        role = org.example.mason.movie.model.enum.Role.USER // Default role for new users
    )
}
