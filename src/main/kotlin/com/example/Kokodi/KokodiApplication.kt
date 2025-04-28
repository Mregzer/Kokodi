package com.example.Kokodi

import org.mapstruct.MapperConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@EnableWebSecurity
@SpringBootApplication
class KokodiApplication

fun main(args: Array<String>) {
	runApplication<KokodiApplication>(*args)
}
