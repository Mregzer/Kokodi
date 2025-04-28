package com.example.Kokodi.repository;

import com.example.Kokodi.entity.PlayerScore
import com.example.Kokodi.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface PlayerScoreRepository : JpaRepository<PlayerScore, Long> {
    fun findByUser_IdAndGameSession_Id(id: Long, gameSessionId: Long): PlayerScore?
}