package com.example.Kokodi.mapper

import com.example.Kokodi.entity.User
import com.example.Kokodi.dto.UserDto
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants
import org.mapstruct.ReportingPolicy

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
abstract class UserMapper {
    abstract fun toEntity(userDto: UserDto): User
    abstract fun toUserDto(user: User): UserDto
}