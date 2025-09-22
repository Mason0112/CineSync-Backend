package org.example.mason.movie.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.security.Key
import java.util.Date
import kotlin.collections.set

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret}") private val jwtSecret: String,
    @Value("\${jwt.expiration-in-ms}") private val jwtExpirationInMs: Long
){
    private val key: Key = Keys.hmacShaKeyFor(jwtSecret.toByteArray())

    fun generateToken(userDetails: UserDetails): String {
        val now = Date()
        val expiryDate = Date(now.time + jwtExpirationInMs)

        // 將角色添加到 JWT 的 claims 中
        val claims = Jwts.claims().setSubject(userDetails.username)
        claims["roles"] = userDetails.authorities.map { it.authority }

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
    }

    fun getEmailFromJWT(token: String?): String {
        val claims: Claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body
        return claims.subject
    }

    fun validateToken(token: String?): Boolean {
        // Jwts.parser will throw an exception if the token is invalid
        runCatching { Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token) }
            .onSuccess { return true }
        return false
    }

    fun getAuthentication(token: String): Authentication {
        val claims: Claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body

        val email = claims.subject
        val roles = claims["roles"] as? List<*> ?: emptyList<String>()

        val authorities = roles.map { SimpleGrantedAuthority(it.toString()) }

        // 建立 Authentication 物件
        return UsernamePasswordAuthenticationToken(email, null, authorities)
    }
}