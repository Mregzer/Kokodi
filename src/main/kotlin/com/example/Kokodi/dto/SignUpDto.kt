package com.example.Kokodi.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

class SignUpDto(
    @NotBlank
    @Pattern(regexp = "^(?=.{4,20}\$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])\$")
    val username: String,
    @NotBlank
    @Pattern(regexp = "^(?=.{4,20}\$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])\$")
    val login: String,
    @NotBlank
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&*-]).{8,}\$")
    val password: String)