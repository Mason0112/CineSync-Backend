package org.example.mason.movie.service

import org.example.mason.movie.mapper.toDto
import org.example.mason.movie.mapper.toEntity
import org.example.mason.movie.mapper.toUserPrincipal
import org.example.mason.movie.model.dto.UserRegisterDto
import org.example.mason.movie.model.dto.UsersDto
import org.example.mason.movie.model.enum.UsersRole
import org.example.mason.movie.repo.UsersRepository
import org.example.mason.movie.security.UsersPrincipal
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

/**
 * 使用者業務邏輯服務
 */
@Service
class UsersService(
    private val userRepository: UsersRepository,
    private val passwordEncoder: PasswordEncoder
) {

    /**
     * 根據 email 查詢使用者資訊
     */
    fun getUserDtoByEmail(email: String): UsersDto {
        val user = userRepository.findByEmail(email)
            .orElseThrow { UsernameNotFoundException("User not found with email: $email") }
        return user.toDto()
    }

    /**
     * 註冊新使用者並回傳 UsersPrincipal
     * @throws IllegalArgumentException 如果 email 已存在
     */
    fun createUserAndGetDetails(regDto: UserRegisterDto): UsersPrincipal {
        if (userRepository.findByEmail(regDto.email).isPresent) {
            throw IllegalArgumentException("Email already exists: ${regDto.email}")
        }

        val encodedPassword = passwordEncoder.encode(regDto.password)
        val defaultRole = UsersRole.USER
        val newUser = regDto.toEntity(
            encodedPassword = encodedPassword,
            role = defaultRole
        )

        val savedUser = userRepository.save(newUser)
        val authorities = listOf(SimpleGrantedAuthority("ROLE_${savedUser.usersRole.name}"))

        return savedUser.toUserPrincipal(authorities = authorities)
    }
}
