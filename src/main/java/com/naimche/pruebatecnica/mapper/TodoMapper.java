package com.naimche.pruebatecnica.mapper;

import com.naimche.pruebatecnica.dto.TodoDto;
import com.naimche.pruebatecnica.entity.Todo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TodoMapper {

    TodoMapper INSTANCE = Mappers.getMapper(TodoMapper.class);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.address.country", target = "country")
    @Mapping(source = "user.username", target = "username")
    TodoDto toDto(Todo todo);


    @Mapping(target = "user", ignore = true)
    Todo toEntity(TodoDto todoDto);
}
