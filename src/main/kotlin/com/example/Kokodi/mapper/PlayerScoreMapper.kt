package com.example.Kokodi.mapper

import com.example.Kokodi.dto.PlayerScoreDto
import com.example.Kokodi.entity.PlayerScore
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants
import org.mapstruct.ReportingPolicy

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
    uses = [UserMapper::class]
)
abstract class PlayerScoreMapper {
    abstract fun toEntity(playerScoreDto: PlayerScoreDto): PlayerScore
    abstract fun toPlayerScoreDto(playerScore: PlayerScore): PlayerScoreDto
}