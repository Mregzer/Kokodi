package com.example.Kokodi.dto


class TurnDto(
    var player: UserDto = UserDto(),
    var target: UserDto? = null,
    var card: CardDto = CardDto()
)