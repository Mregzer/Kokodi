package com.example.Kokodi.controller

import com.example.Kokodi.dto.GameSessionDto
import com.example.Kokodi.service.GameSessionService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/sessions")
class GameSessionController(
    private val service: GameSessionService
) {
    @GetMapping("/{sessionId}")
    fun findSessionById(@PathVariable sessionId: Long): ResponseEntity<GameSessionDto> =
        ResponseEntity
            .status(HttpStatus.OK)
            .body(service.findById(sessionId))

    @PostMapping
    fun createGameSession(): ResponseEntity<GameSessionDto> =
        ResponseEntity
            .status(HttpStatus.CREATED)
            .body(service.createGameSession())

    @PostMapping("/{sessionId}")
    fun joinToSession(@PathVariable sessionId: Long): ResponseEntity<GameSessionDto> =
                ResponseEntity
            .status(HttpStatus.OK)
            .body(service.joinToSession(sessionId))

    @PostMapping("/{sessionId}/start")
    fun startSession(@PathVariable sessionId: Long): ResponseEntity<GameSessionDto> =
                ResponseEntity
            .status(HttpStatus.OK)
            .body(service.startSession(sessionId))
}