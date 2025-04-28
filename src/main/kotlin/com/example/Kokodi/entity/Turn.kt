package com.example.Kokodi.entity

import jakarta.persistence.*

@Entity
@Table(name = "turns")
class Turn(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @ManyToOne
    @JoinColumn(name = "player")
    var player: User = User(),
    @ManyToOne(optional = true)
    @JoinColumn(name = "target")
    var target: User? = null,
    @ManyToOne
    @JoinColumn(name = "card_name")
    var card: Card = Card(),
    @ManyToOne(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "sessions_id")
    var session: GameSession = GameSession()
)