package com.example.Kokodi.controller

import com.example.Kokodi.dto.*
import com.example.Kokodi.service.AuthenticationService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authenticationService: AuthenticationService
) {
    @PostMapping("/login")
    fun authenticate(
        @RequestBody authRequest: SignInRequest
    ): SignInResponse =
        authenticationService.authentication(authRequest)

    @PostMapping("/refresh")
    fun refreshAccessToken(
        @RequestBody request: TokenDto
    ): TokenDto = TokenDto(token = authenticationService.refreshAccessToken(request.token))

    @PostMapping("/register")
    fun saveUser(
        @RequestBody signUpDto: SignUpDto
    ): UserDto = authenticationService.saveUser(signUpDto)
}