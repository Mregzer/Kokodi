package com.example.Kokodi.mapper

import com.example.Kokodi.dto.GameSessionDto
import com.example.Kokodi.entity.Card
import com.example.Kokodi.entity.GameSession
import org.mapstruct.*

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
    uses = [UserMapper::class, TurnMapper::class, PlayerScoreMapper::class]
)
abstract class GameSessionMapper {
    abstract fun toEntity(gameSessionDto: GameSessionDto): GameSession

    @Mapping(target = "deckSize", source = "deck", qualifiedByName = ["getDeckSize"])
    abstract fun toGameSessionDto(gameSession: GameSession): GameSessionDto

    @Named("getDeckSize")
    fun getDeckSize(deck: MutableList<Card>): Int = deck.size
}