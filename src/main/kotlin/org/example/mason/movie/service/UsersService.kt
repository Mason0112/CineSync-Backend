package org.example.mason.movie.service

import org.example.mason.movie.mapper.toDto
import org.example.mason.movie.mapper.toEntity
import org.example.mason.movie.model.dto.UserRegisterDto
import org.example.mason.movie.model.dto.UsersDto
import org.example.mason.movie.model.entity.Users
import org.example.mason.movie.model.enum.UsersRole
import org.example.mason.movie.repo.UsersRepository
import org.example.mason.movie.security.UsersPrincipal
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UsersService(
    private val userRepository: UsersRepository,
    private val passwordEncoder: PasswordEncoder
) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails { // 回傳型別維持 UserDetails
        val user = userRepository.findByEmail(email)
            .orElseThrow { UsernameNotFoundException("User not found with email: $email") }

        val authorities = listOf(SimpleGrantedAuthority("ROLE_${user.usersRole.name}"))

        return user.toUserPrincipal(authorities = authorities)
    }

    fun getUserDtoByEmail(username: String): UsersDto {
        val users = userRepository.findByEmail(username)
            .orElseThrow { UsernameNotFoundException("User not found with email: $username") }
        return users.toDto()
    }
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