package org.example.mason.movie.controller

import org.example.mason.movie.model.dto.LoginResponseDto
import org.example.mason.movie.model.dto.UserRegAndLoginDto
import org.example.mason.movie.security.JwtTokenProvider
import org.example.mason.movie.service.UsersService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RequestMapping("/api/auth")
@RestController
class AuthController(
    private val usersService: UsersService,
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider
) {

    @PostMapping("/login")
    fun authenticateUser(@RequestBody loginRequest: UserRegAndLoginDto): ResponseEntity<LoginResponseDto> {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.email, loginRequest.password)
        )
        SecurityContextHolder.getContext().authentication = authentication
        return createLoginResponse(authentication)
    }

    @PostMapping("/register")
    fun registerUser(@RequestBody registerDto: UserRegAndLoginDto): ResponseEntity<LoginResponseDto> {
        // 1. 先建立使用者
        usersService.createUser(registerDto)

        // 2. 建立成功後，立刻為他們進行認證
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(registerDto.email, registerDto.password)
        )
        SecurityContextHolder.getContext().authentication = authentication

        // 3. 產生 Token 並回傳 LoginResponseDto
        return createLoginResponse(authentication, HttpStatus.CREATED)
    }


    private fun createLoginResponse(authentication: Authentication, status: HttpStatus = HttpStatus.OK): ResponseEntity<LoginResponseDto> {
        val userDetails = authentication.principal as org.springframework.security.core.userdetails.UserDetails
        val jwt = jwtTokenProvider.generateToken(userDetails)
        val userDto = usersService.getUserDtoByEmail(userDetails.username)
        val loginResponse = LoginResponseDto(token = jwt, user = userDto)
        return ResponseEntity(loginResponse, status)
    }
}