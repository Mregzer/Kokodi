package com.example.Kokodi.service

import com.example.Kokodi.validator.GameSessionValidator
import com.example.Kokodi.dto.GameSessionDto
import com.example.Kokodi.dto.PlayerScoreDto
import com.example.Kokodi.dto.UserDto
import com.example.Kokodi.entity.GameSession
import com.example.Kokodi.entity.GameSessionStatus
import com.example.Kokodi.entity.PlayerScore
import com.example.Kokodi.entity.User
import com.example.Kokodi.generator.DeckGenerator
import com.example.Kokodi.mapper.GameSessionMapper
import com.example.Kokodi.repository.GameSessionRepository
import com.example.Kokodi.repository.PlayerScoreRepository
import jakarta.persistence.EntityNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import java.util.*

@ExtendWith(MockitoExtension::class)
class GameSessionServiceTest {

    @InjectMocks
    private lateinit var gameSessionService: GameSessionService

    @Mock
    private lateinit var mapper: GameSessionMapper

    @Mock
    private lateinit var gameSessionRepository: GameSessionRepository

    @Mock
    private lateinit var turnService: TurnService

    @Mock
    private lateinit var deckGenerator: DeckGenerator

    @Mock
    private lateinit var playerScoreRepository: PlayerScoreRepository

    @Mock
    private lateinit var authenticationService: AuthenticationService

    @Mock
    private lateinit var gameSessionValidator: GameSessionValidator

    @Test
    fun findByIdPositiveTest() {
        val gameSession = GameSession(id = 1)

        `when`(gameSessionRepository.findById(1L))
            .thenReturn(Optional.of(gameSession))
        `when`(mapper.toGameSessionDto(gameSession))
            .thenReturn(GameSessionDto(id = 1))

        val result = gameSessionService.findById(1L)

        assertThat(result).isNotNull()
        assertEquals(1L, result.id)
    }

    @Test
    fun findByIdNegativeTest() {
        `when`(gameSessionRepository.findById(1L))
            .thenReturn(Optional.empty())

        assertThrows(EntityNotFoundException::class.java,
            { gameSessionService.findById(1L) },
            "GameSession with id 1 not found"
        )
    }

    @Test
    fun createGameSessionPositiveTest() {
        val user = User(id = 1)

        `when`(authenticationService.getUserFromContext())
            .thenReturn(user)
        `when`(gameSessionRepository.save(any()))
            .thenReturn(GameSession(id = 1, players = mutableListOf(user), createdBy = user))
        `when`(mapper.toGameSessionDto(any()))
            .thenAnswer { invocation ->
                val mappedGameSession = invocation.getArgument<GameSession>(0)
                return@thenAnswer GameSessionDto(id = mappedGameSession.id,
                    players = mappedGameSession.players.map { UserDto(id = it.id) }.toMutableList(),
                    createdBy = UserDto(id = mappedGameSession.createdBy.id)
                )
            }

        val result = gameSessionService.createGameSession()

        assertThat(result).isNotNull()
        assertEquals(1L, result.id)
        assertEquals(1, result.players.size)
        assertEquals(user.id, result.players.first().id)
        assertEquals(user.id, result.createdBy.id)
    }

    @Test
    fun joinToSessionPositiveTest() {
        val firstUser = User(id = 1)
        val secondUser = User(id = 2)
        val gameSession = GameSession(
            id = 1,
            players = mutableListOf(firstUser),
            createdBy = firstUser
        )

        `when`(authenticationService.getUserFromContext())
            .thenReturn(secondUser)
        `when`(gameSessionRepository.findById(1L))
            .thenReturn(Optional.of(gameSession))
        `when`(mapper.toGameSessionDto(any()))
            .thenAnswer { invocation ->
                val mappedGameSession = invocation.getArgument<GameSession>(0)
                return@thenAnswer GameSessionDto(id = mappedGameSession.id,
                    players = mappedGameSession.players.map { UserDto(id = it.id) }.toMutableList(),
                    createdBy = UserDto(id = mappedGameSession.createdBy.id)
                )
            }

        val result = gameSessionService.joinToSession(1L)

        assertThat(result).isNotNull()
        assertEquals(1L, result.id)
        assertEquals(2, result.players.size)
        assertEquals(secondUser.id, result.players[1].id)
        assertEquals(firstUser.id, result.createdBy.id)
    }

    @Test
    fun joinToSessionNegativeTest() {
        `when`(gameSessionRepository.findById(1L))
            .thenReturn(Optional.empty())

        assertThrows(EntityNotFoundException::class.java,
            { gameSessionService.findById(1L) },
            "GameSession with id 1 not found"
        )
    }

    @Test
    fun startSessionPositiveTest() {
        val firstUser = User(id = 1)
        val secondUser = User(id = 2)
        val gameSession = GameSession(
            id = 1,
            status = GameSessionStatus.WAIT_FOR_PLAYERS,
            players = mutableListOf(firstUser, secondUser),
            createdBy = firstUser
        )

        `when`(authenticationService.getUserFromContext())
            .thenReturn(firstUser)
        `when`(gameSessionRepository.findById(1L))
            .thenReturn(Optional.of(gameSession))
        `when`(deckGenerator.generateDeckByPlayerCount(any()))
            .thenReturn(mutableListOf())
        `when`(playerScoreRepository.saveAll(any<List<PlayerScore>>()))
            .thenAnswer{ it.getArgument<List<PlayerScore>>(0) }
        `when`(mapper.toGameSessionDto(any()))
            .thenAnswer { invocation ->
                val mappedGameSession = invocation.getArgument<GameSession>(0)
                return@thenAnswer GameSessionDto(id = mappedGameSession.id,
                    players = mappedGameSession.players.map { UserDto(id = it.id) }.toMutableList(),
                    status = mappedGameSession.status,
                    scoreBoard = mappedGameSession.scoreBoard.map {
                        PlayerScoreDto(user = UserDto(it.user.id), score = it.score) }.toMutableList(),
                    createdBy = UserDto(id = mappedGameSession.createdBy.id
                    )
                )
            }

        val result = gameSessionService.startSession(1L)
        assertThat(result).isNotNull()
        assertThat(result.status).isEqualTo(GameSessionStatus.IN_PROGRESS)
        assertThat(result.scoreBoard).isNotEmpty()
        assertThat(result.scoreBoard.size).isEqualTo(2)
        assertThat(result.scoreBoard[0].user.id).isEqualTo(firstUser.id)
        assertThat(result.scoreBoard[1].user.id).isEqualTo(secondUser.id)
    }

    @Test
    fun startSessionNegativeTest() {
        `when`(gameSessionRepository.findById(1L))
            .thenReturn(Optional.empty())

        assertThrows(EntityNotFoundException::class.java,
            { gameSessionService.findById(1L) },
            "GameSession with id 1 not found"
        )
    }
}