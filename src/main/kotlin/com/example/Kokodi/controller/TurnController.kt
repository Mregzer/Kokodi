package com.example.Kokodi.controller

import com.example.Kokodi.dto.TurnDto
import com.example.Kokodi.service.TurnService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/sessions")
class TurnController(
    private val turnService: TurnService
) {
    @PostMapping("/{sessionId}/turn")
    fun makeAGameMove(@PathVariable sessionId: Long, @RequestParam targetId: Long?): ResponseEntity<TurnDto> =
        ResponseEntity
            .status(HttpStatus.OK)
            .body(turnService.makeAGameMove(targetId, sessionId))
}