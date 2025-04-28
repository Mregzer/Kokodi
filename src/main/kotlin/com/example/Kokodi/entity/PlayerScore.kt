package com.example.Kokodi.entity

import jakarta.persistence.*

@Entity
@Table(name = "player_scores")
data class PlayerScore(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User = User(),
    @ManyToOne
    @JoinColumn(name = "game_session_id")
    var gameSession: GameSession = GameSession(),
    @Column(name = "score")
    var score: Int = 0
)