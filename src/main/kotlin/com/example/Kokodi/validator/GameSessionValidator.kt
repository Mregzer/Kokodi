package com.example.Kokodi.validator

import com.example.Kokodi.entity.GameSession
import com.example.Kokodi.entity.GameSessionStatus
import com.example.Kokodi.entity.User
import com.example.Kokodi.exception.DoubleConnectionException
import com.example.Kokodi.exception.GameNotStartedException
import org.springframework.stereotype.Component

@Component
class GameSessionValidator {
    fun maxPlayersValidation(gameSession: GameSession) {
        if (gameSession.players.size > 4) {
            throw GameNotStartedException(
                "Game session must have at most 4 players"
            )
        }
    }

    fun creatorSessionValidation(user: User, sessionId: Long) {
        if (user.createdSession?.find { it.id == sessionId } == null)
            throw GameNotStartedException("Start the game session can creator only")
    }

    fun doubleConnectionValidation(gameSession: GameSession, user: User) {
        if (gameSession.players.contains(user))
            throw DoubleConnectionException(
                "Player with id ${user.id} already connected to game session ${gameSession.id}"
            )
    }

    fun minPlayersForStartValidation(gameSession: GameSession) {
        if (gameSession.players.size < 2) {
            throw GameNotStartedException(
                "Game session must have at least 2 players"
            )
        }
    }

    fun statusByStartingValidation(gameSession: GameSession) {
        if (gameSession.status != GameSessionStatus.WAIT_FOR_PLAYERS) {
            throw GameNotStartedException(
                "Game session must have status WAIT_FOR_PLAYERS"
            )
        }
    }
}