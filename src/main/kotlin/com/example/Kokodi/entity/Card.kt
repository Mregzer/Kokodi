package com.example.Kokodi.entity

import jakarta.persistence.*

@Entity
@Table(name = "cards")
class Card(
    @Id
    var name: String = "",
    @Column(name = "value")
    var value: Int = 0,
    @Column(name = "card_type")
    @Enumerated(EnumType.STRING)
    var cardType: CardType = CardType.POINTS,
    @ManyToMany(mappedBy = "deck")
    var sessions: MutableList<GameSession> = mutableListOf(),
    @OneToMany(mappedBy = "card")
    var turns: MutableList<Turn> = mutableListOf()
)