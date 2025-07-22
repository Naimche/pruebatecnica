package com.naimche.pruebatecnica.service;

import com.naimche.pruebatecnica.dto.CreateUserDto;
import com.naimche.pruebatecnica.dto.UserDto;
import com.naimche.pruebatecnica.exception.auth.UserNameAlreadyExistsException;
import com.naimche.pruebatecnica.exception.auth.WeakPasswordException;
import com.naimche.pruebatecnica.mapper.UserMapper;
import com.naimche.pruebatecnica.repository.AuthRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthRepository authRepository;

    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public UserDto registerUser(CreateUserDto createUserDto) {

        if (createUserDto.getPassword() == null ||
                createUserDto.getPassword().length() < 8 ||
                !createUserDto.getPassword().matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            throw new WeakPasswordException();
        }
        if (authRepository.findByUsername(createUserDto.getUsername()).isPresent()) {
            throw new UserNameAlreadyExistsException();
        }
        var user = authRepository.save(UserMapper.INSTANCE.toEntity(createUserDto));
        return UserMapper.INSTANCE.toDto(user);
    }
}
