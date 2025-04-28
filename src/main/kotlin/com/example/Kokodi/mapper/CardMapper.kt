package com.example.Kokodi.mapper

import com.example.Kokodi.dto.CardDto
import com.example.Kokodi.entity.Card
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants
import org.mapstruct.ReportingPolicy

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
abstract class CardMapper {
    abstract fun toEntity(cardDto: CardDto): Card
    abstract fun toCardDto(card: Card): CardDto
}