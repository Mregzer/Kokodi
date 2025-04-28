package com.example.Kokodi.entity

import jakarta.persistence.*

@Entity
@Table(name = "sessions")
class GameSession(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @ManyToMany
    @JoinTable(name = "users_sessions",
        joinColumns = [JoinColumn(name = "session_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")])
    var players: MutableList<User> = mutableListOf(),
    @OneToMany(mappedBy = "gameSession", cascade = [CascadeType.ALL])
    var scoreBoard: MutableList<PlayerScore> = mutableListOf(),
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    var status: GameSessionStatus = GameSessionStatus.WAIT_FOR_PLAYERS,
    @ManyToMany
    @JoinTable(name = "cards_sessions",
        joinColumns = [JoinColumn(name = "session_id")],
        inverseJoinColumns = [JoinColumn(name = "card_id")])
    var deck: MutableList<Card> = mutableListOf(),
    @OneToMany(mappedBy = "session", cascade = [CascadeType.ALL])
    @OrderBy("id DESC")
    var turnHistory: MutableList<Turn> = mutableListOf(),
    @ManyToOne
    @JoinColumn(name = "created_by")
    var createdBy: User = User()
)
