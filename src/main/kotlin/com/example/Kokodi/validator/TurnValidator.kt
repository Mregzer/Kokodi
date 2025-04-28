package com.example.Kokodi.validator

import com.example.Kokodi.entity.GameSession
import com.example.Kokodi.entity.GameSessionStatus
import com.example.Kokodi.entity.Turn
import com.example.Kokodi.entity.User
import org.springframework.stereotype.Component

@Component
class TurnValidator {

    fun sessionStatusForTurnValidation(gameSession: GameSession) {
        if (gameSession.status != GameSessionStatus.IN_PROGRESS) {
            throw IllegalStateException(
                "Game session is not in progress"
            )
        }
    }

    fun targetIdValidation(targetId: Long?, currentPlayerId: Long) {
        if (targetId == null || targetId == currentPlayerId)
            throw IllegalArgumentException(
                "Steal cards cannot be used without a target or on yourself"
            )
    }


    fun playerPresenceValidation(gameSession: GameSession, userId: Long) {
        gameSession.players.find { it.id == userId }
            ?: throw NoSuchElementException("The player with id $userId is not present in the current session")
    }

    fun playerQueueForTurnValidation(turn: Turn, user: User) {
        if (turn.player != user)
            throw IllegalStateException(
                "It's another player's turn now"
            )
    }
}
