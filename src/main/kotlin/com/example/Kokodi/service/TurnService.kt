package com.example.Kokodi.service

import com.example.Kokodi.validator.TurnValidator
import com.example.Kokodi.dto.GameSessionDto
import com.example.Kokodi.dto.TurnDto
import com.example.Kokodi.entity.*
import com.example.Kokodi.entity.Card
import com.example.Kokodi.entity.CardType
import com.example.Kokodi.mapper.GameSessionMapper
import com.example.Kokodi.mapper.TurnMapper
import com.example.Kokodi.repository.GameSessionRepository
import com.example.Kokodi.repository.PlayerScoreRepository
import com.example.Kokodi.repository.TurnRepository
import com.example.Kokodi.repository.UserRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
open class TurnService(
    private val turnRepository: TurnRepository,
    private val authenticationService: AuthenticationService,
    private val gameSessionRepository: GameSessionRepository,
    private val turnMapper: TurnMapper,
    @Value("\${application.gameSession.winScore}")
    private val winScore: Int,
    private val playerScoreRepository: PlayerScoreRepository,
    private val turnValidator: TurnValidator
) {

    @Transactional
    open fun makeAGameMove(targetId: Long?, sessionId: Long): TurnDto {
        val gameSession: GameSession = gameSessionRepository.findById(sessionId).orElseThrow(
            { EntityNotFoundException("GameSession with id $sessionId not found") })

        turnValidator.sessionStatusForTurnValidation(gameSession)

        val user: User = authenticationService.getUserFromContext()
        val actualTurn: Turn = turnRepository.findById(gameSession.turnHistory.first().id!!).get()

        turnValidator.playerPresenceValidation(gameSession, user.id)
        turnValidator.playerQueueForTurnValidation(actualTurn, user)

        val userPlayerScore: PlayerScore =
            playerScoreRepository.findByUser_IdAndGameSession_Id(user.id, gameSession.id!!)!!
        val card: Card = actualTurn.card

        when {
            card.name.equals("Block") -> {
                actualTurn.target = gameSession.players[0]
                gameSession.players.rotateLeft()
            }

            card.name.startsWith("Steal") -> {
                turnValidator.targetIdValidation(targetId, user.id)
                turnValidator.playerPresenceValidation(gameSession, targetId!!)

                val targetPlayerScore: PlayerScore =
                    playerScoreRepository.findByUser_IdAndGameSession_Id(targetId!!, gameSession.id!!)!!
                val possibleSteal =
                    if (targetPlayerScore.score < card.value) targetPlayerScore.score else card.value
                targetPlayerScore.score -= possibleSteal
                userPlayerScore.score += possibleSteal
                actualTurn.target = targetPlayerScore.user
            }

            card.name.equals("DoubleDown") -> {
                userPlayerScore.score *= 2
                actualTurn.target = user
            }

            else -> {
                userPlayerScore.score += card.value
                actualTurn.target = user
            }
        }

        if (gameSession.scoreBoard.find { it.score >= winScore } != null) {
            gameSession.status = GameSessionStatus.FINISHED
        } else getNextTurn(gameSession)

        return turnMapper.toTurnDto(actualTurn)
    }

    @Transactional
    open fun getNextTurn(gameSession: GameSession) {
        val card: Card = gameSession.deck.removeAt((0..gameSession.deck.size).random())
        val turn: Turn = Turn(player = gameSession.players[0], card = card, session = gameSession)
        turnRepository.save(turn)
        gameSession.players.rotateLeft()
    }


    fun <T> MutableList<T>.rotateLeft() {
        if (size > 1) {
            val firstElement = removeAt(0)
            add(firstElement)
        }
    }
}