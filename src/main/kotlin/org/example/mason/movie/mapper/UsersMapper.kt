package org.example.mason.movie.mapper

import org.example.mason.movie.model.dto.UserRegisterDto
import org.example.mason.movie.model.dto.UsersDto
import org.example.mason.movie.model.entity.Users
import org.example.mason.movie.security.UsersPrincipal
import org.springframework.security.core.GrantedAuthority

/**
 * Users Entity 轉換為 DTO
 */
fun Users.toDto(): UsersDto {
    return UsersDto(
        id = this.id!!,
        userName = this.userName,
        email = this.email,
        usersRole = this.usersRole
    )
}

/**
 * Users Entity 轉換為 Spring Security 的 UserPrincipal
 */
fun Users.toUserPrincipal(authorities: Collection<GrantedAuthority>): UsersPrincipal {
    val userId = this.id ?: throw IllegalStateException("User ID cannot be null for UserPrincipal conversion")
    return UsersPrincipal(
        id = userId,
        userName = this.userName,
        email = this.email,
        passwordHash = this.password,
        authoritiesCollection = authorities
    )
}

/**
 * UserRegisterDto 轉換為 Users Entity
 * @param encodedPassword 已編碼的密碼
 * @param role 使用者角色
 */
fun UserRegisterDto.toEntity(
    encodedPassword: String,
    role: org.example.mason.movie.model.enum.UsersRole
): Users {
    return Users(
        id = null,
        userName = this.userName,
        email = this.email,
        password = encodedPassword,
        usersRole = role
    )
}

