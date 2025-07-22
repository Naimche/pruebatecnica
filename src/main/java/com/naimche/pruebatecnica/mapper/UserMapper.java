package com.naimche.pruebatecnica.mapper;

import com.naimche.pruebatecnica.dto.CreateUserDto;
import com.naimche.pruebatecnica.dto.UserDto;
import com.naimche.pruebatecnica.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toDto(User User);

    User toEntity(CreateUserDto createUserDto);

}
