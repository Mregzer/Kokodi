package com.example.Kokodi.service

import com.example.Kokodi.exception.UserLoginNotFoundException
import com.example.Kokodi.repository.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class JwtUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(login: String): UserDetails {
        val user = userRepository.findByLogin(login)
            ?: throw UserLoginNotFoundException("$login")

        return User.builder()
            .username(user.login)
            .password(user.password)
            .roles(user.role.name)
            .build()
    }
}