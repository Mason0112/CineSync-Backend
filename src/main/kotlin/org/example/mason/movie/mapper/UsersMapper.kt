package org.example.mason.movie.mapper

import org.example.mason.movie.model.dto.UsersDto
import org.example.mason.movie.model.dto.UserRegisterDto
import org.example.mason.movie.model.entity.Users

fun Users.toDto(): UsersDto {
    return UsersDto(
        id = this.id ?: throw IllegalStateException("User ID cannot be null for DTO conversion"),
        userName = this.userName,
        email = this.email,
        usersRole = this.usersRole
    )
}

fun UserRegisterDto.toEntity(): Users {
    return Users(
        userName = this.email, // Assuming email is used as userName for registration
        email = this.email,
        password = this.password,
        usersRole = org.example.mason.movie.model.enum.UsersRole.USER // Default role for new users
    )
}
