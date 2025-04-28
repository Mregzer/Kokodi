package com.example.Kokodi.dto

import com.example.Kokodi.entity.CardType

class CardDto(
 var name: String = "",
 var value: Int = 0,
 var cardType: CardType = CardType.POINTS
)