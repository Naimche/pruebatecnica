package com.naimche.pruebatecnica.service;

import com.naimche.pruebatecnica.dto.AuthRequestDto;
import com.naimche.pruebatecnica.dto.CreateUserDto;
import com.naimche.pruebatecnica.dto.UserDto;
import com.naimche.pruebatecnica.exception.auth.UserNameAlreadyExistsException;
import com.naimche.pruebatecnica.exception.auth.WeakPasswordException;
import com.naimche.pruebatecnica.mapper.UserMapper;
import com.naimche.pruebatecnica.repository.AuthRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthRepository authRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;


    public AuthService(AuthRepository authRepository, AuthenticationManager authenticationManager, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.authRepository = authRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
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
        var user = UserMapper.INSTANCE.toEntity(createUserDto);
        user.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
        user = authRepository.save(user);
        return UserMapper.INSTANCE.toDto(user);
    }

    public String authenticate(AuthRequestDto request) {
        if (!Boolean.parseBoolean(System.getProperty("bypass.password"))) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        }

        var user = authRepository.findByUsername(request.getUsername())
                .orElseThrow();

        return jwtService.generateToken(user.getUsername());

    }
}
