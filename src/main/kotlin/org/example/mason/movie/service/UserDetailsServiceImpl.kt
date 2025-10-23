package org.example.mason.movie.service

import org.example.mason.movie.mapper.toUserPrincipal
import org.example.mason.movie.repo.UsersRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

/**
 * Spring Security 專用的 UserDetailsService 實作
 */
@Service
class UserDetailsServiceImpl(
    private val userRepository: UsersRepository
) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        val user = userRepository.findByEmail(email)
            .orElseThrow { UsernameNotFoundException("User not found with email: $email") }

        val authorities = listOf(SimpleGrantedAuthority("ROLE_${user.usersRole.name}"))

        return user.toUserPrincipal(authorities = authorities)
    }
}

