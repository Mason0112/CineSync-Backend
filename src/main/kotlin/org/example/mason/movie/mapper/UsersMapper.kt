package org.example.mason.movie.mapper

import org.example.mason.movie.model.dto.UsersDto
import org.example.mason.movie.model.dto.UserRegisterDto
import org.example.mason.movie.model.entity.Users
import org.example.mason.movie.model.enum.UsersRole

// ✅ Entity → DTO (讀取資料時使用)
fun Users.toDto(): UsersDto {
    return UsersDto(
        id = this.id ?: throw IllegalStateException("User ID cannot be null for DTO conversion"),
        userName = this.userName,
        email = this.email,
        usersRole = this.usersRole
    )
}

// ✅ DTO → Entity (註冊時使用,需要傳入加密後的密碼和角色)
fun UserRegisterDto.toEntity(
    encodedPassword: String,
    role: UsersRole = UsersRole.USER
): Users {
    return Users(
        id = null,
        userName = this.userName,
        email = this.email,
        password = encodedPassword,
        usersRole = role
    )
}

// ✅ 可選:直接從欄位建立 Entity (用於測試或特殊場景)
fun createUser(
    userName: String,
    email: String,
    encodedPassword: String,
    role: UsersRole = UsersRole.USER
): Users {
    return Users(
        id = null,
        userName = userName,
        email = email,
        password = encodedPassword,
        usersRole = role
    )
}
