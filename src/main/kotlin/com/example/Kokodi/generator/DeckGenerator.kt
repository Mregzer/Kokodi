package com.example.Kokodi.generator

import com.example.Kokodi.entity.Card
import com.example.Kokodi.repository.CardRepository
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component

@Component
class DeckGenerator(private val cardRepository: CardRepository) {
    private var deck: List<Card> = listOf()

    @PostConstruct
    fun init(): Unit {
        deck = cardRepository.findAll()
    }

    fun generateDeckByPlayerCount(num: Int): MutableList<Card> {
        val newDeck: MutableList<Card> = mutableListOf()
        for (i in 1..4) newDeck.addAll(deck)
        return newDeck
    }
}