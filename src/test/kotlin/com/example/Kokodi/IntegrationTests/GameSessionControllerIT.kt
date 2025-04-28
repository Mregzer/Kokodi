package com.example.Kokodi.IntegrationTests;

import com.example.Kokodi.entity.GameSessionStatus
import com.example.Kokodi.repository.GameSessionRepository
import com.example.Kokodi.service.GameSessionService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql(scripts = ["classpath:cleanup.sql", "classpath:data.sql"],
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
open class GameSessionControllerIT @Autowired constructor(
    val gameSessionRepository: GameSessionRepository
) : AbstractIntegrationTest() {

    @Test
    @Throws(Exception::class)
    fun findSessionById() {
        mockMvc.perform(
            get("/api/v1/sessions/{0}", "1")
                .with(
                    SecurityMockMvcRequestPostProcessors.user("user1")
                        .password("Pass111)")
                )
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("1"))
            .andExpect(jsonPath("$.status").value("WAIT_FOR_PLAYERS"))
            .andExpect(jsonPath("$.createdBy.id").value("1"))
    }

    @Test
    @Throws(java.lang.Exception::class)
    open fun createGameSession() {
        mockMvc.perform(
            post("/api/v1/sessions")
                .with(
                    SecurityMockMvcRequestPostProcessors.user("user2")
                        .password("Pass222)")
                )
        )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value("2"))
            .andExpect(jsonPath("$.status").value("WAIT_FOR_PLAYERS"))
            .andExpect(jsonPath("$.createdBy.id").value("2"))

        assert(gameSessionRepository.existsById(2))
    }

    @Test
    @Throws(java.lang.Exception::class)
    open fun joinToSession() {
        mockMvc.perform(
            post("/api/v1/sessions/{0}", "1")
                .with(
                    SecurityMockMvcRequestPostProcessors.user("user3")
                        .password("Pass333)")
                )
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("1"))
            .andExpect(jsonPath("$.status").value("WAIT_FOR_PLAYERS"))
            .andExpect(jsonPath("$.createdBy.id").value("1"))
            .andExpect(jsonPath("$.players[2].id").value("3"))

        assert(gameSessionRepository.findById(1).get().players.size == 3)
    }

    @Test
    @Throws(java.lang.Exception::class)
    open fun startSession() {
        mockMvc.perform(
            post("/api/v1/sessions/{0}/start", "1")
                .with(
                    SecurityMockMvcRequestPostProcessors.user("user1")
                        .password("Pass111)")
                )
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("1"))
            .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
            .andExpect(jsonPath("$.createdBy.id").value("1"))
            .andExpect(jsonPath("$.scoreBoard[0].user.id").value("1"))
            .andExpect(jsonPath("$.scoreBoard[0].score").value("0"))

        assert(gameSessionRepository.findById(1).get().status == GameSessionStatus.IN_PROGRESS)
    }
}
