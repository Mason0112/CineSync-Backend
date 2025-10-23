package org.example.mason.movie.controller

import org.example.mason.movie.model.dto.UsersDto
import org.example.mason.movie.security.UsersPrincipal
import org.example.mason.movie.service.UsersService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/users")
@RestController
class UsersController(
    private val usersService: UsersService
) {

    @GetMapping("/me")
    fun getCurrentUser(@AuthenticationPrincipal user: UsersPrincipal): ResponseEntity<UsersDto> {
        val userDto = usersService.getUserDtoByEmail(user.username)
        return ResponseEntity.ok(userDto)
    }
}

