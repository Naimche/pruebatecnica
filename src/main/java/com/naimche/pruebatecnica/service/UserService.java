package com.naimche.pruebatecnica.service;

import com.naimche.pruebatecnica.dto.UserDto;
import com.naimche.pruebatecnica.mapper.UserMapper;
import com.naimche.pruebatecnica.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<UserDto> findAll() {
        return repository.findAll().stream().map(
                UserMapper.INSTANCE::toDto
        ).toList();
    }

}
