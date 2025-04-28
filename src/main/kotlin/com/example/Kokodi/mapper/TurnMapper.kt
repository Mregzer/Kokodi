package com.example.Kokodi.mapper

import com.example.Kokodi.dto.TurnDto
import com.example.Kokodi.entity.Turn
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants
import org.mapstruct.ReportingPolicy

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
    uses = [UserMapper::class, CardMapper::class]
)
abstract class TurnMapper {
    abstract fun toEntity(turnDto: TurnDto): Turn
    abstract fun toTurnDto(turn: Turn): TurnDto
}