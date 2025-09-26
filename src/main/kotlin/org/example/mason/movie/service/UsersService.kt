package org.example.mason.movie.service

import org.example.mason.movie.mapper.toDto
import org.example.mason.movie.model.dto.UserRegisterDto
import org.example.mason.movie.model.dto.UsersDto
import org.example.mason.movie.model.entity.Users
import org.example.mason.movie.model.enum.UsersRole
import org.example.mason.movie.repo.UsersRepository
import org.example.mason.movie.security.UsersPrincipal
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

        // 🎯 關鍵變更：回傳您自訂的 UserPrincipal 物件
        return UsersPrincipal(
            id = user.id, // 假設您的 User Entity 有 id 欄位
            userName = user.userName, // 假設您的 User Entity 有 userName 欄位
            email = user.email,
            passwordHash = user.password,
            authoritiesCollection = authorities
        )
    }

    fun getUserDtoByEmail(username: String): UsersDto {
        val users = userRepository.findByEmail(username)
            .orElseThrow { UsernameNotFoundException("User not found with email: $username") }
        return users.toDto()
    }
    fun createUserAndGetDetails(userDto: UserRegisterDto): UsersPrincipal {
        val newUser = Users(userName = userDto.userName, email = userDto.email, password = passwordEncoder.encode(userDto.password), usersRole = UsersRole.USER)
        val savedUser = userRepository.save(newUser)
        val authorities = listOf(SimpleGrantedAuthority("ROLE_${savedUser.usersRole.name}"))
        return UsersPrincipal(
            id = savedUser.id,
            userName = savedUser.userName,
            email = savedUser.email,
            passwordHash = savedUser.password,
            authoritiesCollection = authorities
        )
    }
}