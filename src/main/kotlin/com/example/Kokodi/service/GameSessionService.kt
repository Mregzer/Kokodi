package com.example.Kokodi.service

import com.example.Kokodi.validator.GameSessionValidator
import com.example.Kokodi.dto.GameSessionDto
import com.example.Kokodi.entity.GameSession
import com.example.Kokodi.entity.GameSessionStatus
import com.example.Kokodi.entity.PlayerScore
import com.example.Kokodi.entity.User
import com.example.Kokodi.exception.GameNotStartedException
import com.example.Kokodi.generator.DeckGenerator
import com.example.Kokodi.mapper.GameSessionMapper
import com.example.Kokodi.repository.GameSessionRepository
import com.example.Kokodi.repository.PlayerScoreRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
open class GameSessionService(
    private val gameSessionMapper: GameSessionMapper,
    private val gameSessionRepository: GameSessionRepository,
    private val authenticationService: AuthenticationService,
    private val turnService: TurnService,
    private val deckGenerator: DeckGenerator,
    private val playerScoreRepository: PlayerScoreRepository,
    private val gameSessionValidator: GameSessionValidator
) {

    open fun findById(id: Long): GameSessionDto {
        val gameSession: GameSession = gameSessionRepository.findById(id).orElseThrow(
            { EntityNotFoundException("GameSession with id $id not found") })
            return gameSessionMapper.toGameSessionDto(gameSession)
    }

    open fun createGameSession(): GameSessionDto {
        val user: User = authenticationService.getUserFromContext()
        val players: ArrayDeque<User> = ArrayDeque()
        players.add(user)
        val gameSession: GameSession = gameSessionRepository.save(
            GameSession(players = players, createdBy = user)
        )

        return gameSessionMapper.toGameSessionDto(gameSession)
    }

    open fun joinToSession(id: Long): GameSessionDto {
        val gameSession: GameSession = gameSessionRepository.findById(id).orElseThrow(
            { EntityNotFoundException("GameSession with id $id not found") })
        gameSessionValidator.maxPlayersValidation(gameSession)

        val user: User = authenticationService.getUserFromContext()
        gameSessionValidator.doubleConnectionValidation(gameSession, user)
        gameSession.players.add(user)

        return gameSessionMapper.toGameSessionDto(gameSession)
    }

    open fun startSession(id: Long): GameSessionDto {
        gameSessionValidator.creatorSessionValidation(authenticationService.getUserFromContext(), id)

        val gameSession: GameSession = gameSessionRepository.findById(id).orElseThrow(
            { EntityNotFoundException("GameSession with id $id not found") })

        gameSessionValidator.minPlayersForStartValidation(gameSession)
        gameSessionValidator.statusByStartingValidation(gameSession)
        gameSession.deck = deckGenerator.generateDeckByPlayerCount(gameSession.players.size)
        gameSession.status = GameSessionStatus.IN_PROGRESS

        val scoreBoard: MutableList<PlayerScore> = gameSession.players.map {
            PlayerScore(user = it, gameSession = gameSession) }
            .toMutableList()
        gameSession.scoreBoard = playerScoreRepository.saveAll(scoreBoard)

        gameSession.players.shuffle()

        turnService.getNextTurn(gameSession)

        return gameSessionMapper.toGameSessionDto(gameSession)
    }

}