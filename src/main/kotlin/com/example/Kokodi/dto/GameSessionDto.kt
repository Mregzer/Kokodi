package com.example.Kokodi.dto

import com.example.Kokodi.entity.GameSessionStatus


class GameSessionDto(
    var id: Long? = null,
    var players: MutableList<UserDto> = mutableListOf(),
    var status: GameSessionStatus = GameSessionStatus.WAIT_FOR_PLAYERS,
    var deckSize: Int = 0,
    var turnHistory: MutableList<TurnDto> = mutableListOf(),
    var createdBy: UserDto = UserDto(),
    var scoreBoard: MutableList<PlayerScoreDto> = mutableListOf()
)