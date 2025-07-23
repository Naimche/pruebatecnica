package com.naimche.pruebatecnica.mapper;

import com.naimche.pruebatecnica.dto.CreateUserDto;
import com.naimche.pruebatecnica.dto.UserDto;
import com.naimche.pruebatecnica.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toDto(User User);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    User toEntity(CreateUserDto createUserDto);

}
