package com.example.Kokodi.service

import com.example.Kokodi.dto.SignInRequest
import com.example.Kokodi.dto.SignInResponse
import com.example.Kokodi.dto.SignUpDto
import com.example.Kokodi.dto.UserDto
import com.example.Kokodi.entity.User
import com.example.Kokodi.exception.UserAlreadyExistException
import com.example.Kokodi.exception.UserLoginNotFoundException
import com.example.Kokodi.mapper.UserMapper
import com.example.Kokodi.repository.RefreshTokenRepository
import com.example.Kokodi.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
open class AuthenticationService(
    private val authManager: AuthenticationManager,
    private val userDetailsService: UserDetailsService,
    private val tokenService: TokenService,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val userRepository: UserRepository,
    @Autowired private val userMapper: UserMapper,
    private val encoder: PasswordEncoder,
    @Value("\${application.jwt.accessTokenExpiration}")
    private val accessTokenExpiration: Long = 0,
    @Value("\${application.jwt.refreshTokenExpiration}")
    private val refreshTokenExpiration: Long = 0
) {


    open fun saveUser(signUpDto: SignUpDto): UserDto {

        if (userRepository.existsByLogin(signUpDto.login)) {
            throw UserAlreadyExistException(signUpDto.login)
        }
        val newUser: User = userRepository.save(
            User(
                username = signUpDto.username,
                login = signUpDto.login,
                password = encoder.encode(signUpDto.password)
            )
        )
        return userMapper.toUserDto(newUser)
    }

    open fun getUserFromContext(): User {
        val authentication = SecurityContextHolder.getContext().authentication
        val userDetails = authentication.principal as UserDetails
        return userRepository.findByLogin(userDetails.username)
            ?: throw UserLoginNotFoundException(userDetails.username)
    }

    open fun authentication(signInRequest: SignInRequest): SignInResponse {
        authManager.authenticate(
            UsernamePasswordAuthenticationToken(
                signInRequest.login,
                signInRequest.password
            )
        )

        val user = userDetailsService.loadUserByUsername(signInRequest.login)

        val accessToken = createAccessToken(user)
        val refreshToken = createRefreshToken(user)


        refreshTokenRepository.save(refreshToken, user)

        return SignInResponse(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    open fun refreshAccessToken(refreshToken: String): String {
        val login = tokenService.extractLogin(refreshToken)

        return login.let { user ->
            val currentUserDetails = userDetailsService.loadUserByUsername(user)
            val refreshTokenUserDetails = refreshTokenRepository.findUserDetailsByToken(refreshToken)

            if (currentUserDetails.username == refreshTokenUserDetails?.username)
                createAccessToken(currentUserDetails)
            else
                throw AuthenticationServiceException("Invalid refresh token")
        }
    }

    private fun createAccessToken(user: UserDetails): String {
        return tokenService.generateToken(
            subject = user.username,
            expiration = Date(System.currentTimeMillis() + accessTokenExpiration)
        )
    }

    private fun createRefreshToken(user: UserDetails) = tokenService.generateToken(
        subject = user.username,
        expiration = Date(System.currentTimeMillis() + refreshTokenExpiration)
    )
}