package org.example.mason.movie.security

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class UsersPrincipal(
    val id: Long?,
    val userName: String,
    private val email: String,
    @JsonIgnore // 在序列化成 JSON 時忽略密碼
    private val passwordHash: String,
    private val authoritiesCollection: Collection<GrantedAuthority>
) : UserDetails {

    // UserDetails 介面要求實作的方法
    override fun getAuthorities(): Collection<GrantedAuthority> = authoritiesCollection

    override fun getPassword(): String = passwordHash

    override fun getUsername(): String = email // Spring Security 的 "username" 我們用 email 來扮演

    // 其他方法，通常直接回傳 true 即可
    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true



    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}