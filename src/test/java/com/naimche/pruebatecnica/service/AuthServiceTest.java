package com.naimche.pruebatecnica.service;

import com.naimche.pruebatecnica.dto.AuthRequestDto;
import com.naimche.pruebatecnica.dto.CreateUserDto;
import com.naimche.pruebatecnica.dto.UserDto;
import com.naimche.pruebatecnica.entity.User;
import com.naimche.pruebatecnica.exception.auth.UserNameAlreadyExistsException;
import com.naimche.pruebatecnica.exception.auth.WeakPasswordException;
import com.naimche.pruebatecnica.mapper.UserMapper;
import com.naimche.pruebatecnica.repository.AuthRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService Tests")
@TestPropertySource(properties = "bypass.password=false")
class AuthServiceTest {

    @Mock
    private AuthRepository authRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private CreateUserDto validCreateUserDto;
    private User mockUser;
    private UserDto mockUserDto;

    @BeforeEach
    void setUp() {
        validCreateUserDto = new CreateUserDto();
        validCreateUserDto.setUsername("testuser");
        validCreateUserDto.setPassword("Password123!");

        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");
        mockUser.setPassword("encodedPassword");

        mockUserDto = new UserDto();
        mockUserDto.setId(1L);
        mockUserDto.setUsername("testuser");
    }

    @Nested
    @DisplayName("registerUser Tests")
    class RegisterUserTests {


        @Test
        @DisplayName("Should throw WeakPasswordException when password is null")
        void shouldThrowWeakPasswordException_WhenPasswordIsNull() {
            // Given
            CreateUserDto invalidDto = new CreateUserDto();
            invalidDto.setUsername("testuser");
            invalidDto.setPassword(null);

            // When & Then
            assertThrows(WeakPasswordException.class,
                    () -> authService.registerUser(invalidDto));

            verifyNoInteractions(authRepository, passwordEncoder);
        }

        @Test
        @DisplayName("Should throw WeakPasswordException when password is too short")
        void shouldThrowWeakPasswordException_WhenPasswordTooShort() {
            // Given
            CreateUserDto invalidDto = new CreateUserDto();
            invalidDto.setUsername("testuser");
            invalidDto.setPassword("Pass1!");

            // When & Then
            assertThrows(WeakPasswordException.class,
                    () -> authService.registerUser(invalidDto));

            verifyNoInteractions(authRepository, passwordEncoder);
        }

        @Test
        @DisplayName("Should throw WeakPasswordException when password has no special characters")
        void shouldThrowWeakPasswordException_WhenNoSpecialCharacters() {
            // Given
            CreateUserDto invalidDto = new CreateUserDto();
            invalidDto.setUsername("testuser");
            invalidDto.setPassword("Password123");

            // When & Then
            assertThrows(WeakPasswordException.class,
                    () -> authService.registerUser(invalidDto));

            verifyNoInteractions(authRepository, passwordEncoder);
        }

        @Test
        @DisplayName("Should throw UserNameAlreadyExistsException when username exists")
        void shouldThrowUserNameAlreadyExistsException_WhenUsernameExists() {
            // Given
            when(authRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));

            // When & Then
            assertThrows(UserNameAlreadyExistsException.class,
                    () -> authService.registerUser(validCreateUserDto));

            verify(authRepository).findByUsername("testuser");
            verifyNoInteractions(passwordEncoder);
            verify(authRepository, never()).save(any());
        }

    }

    @Nested
    @DisplayName("authenticate Tests")
    class AuthenticateTests {

        private AuthRequestDto authRequest;

        @BeforeEach
        void setUp() {
            authRequest = new AuthRequestDto();
            authRequest.setUsername("testuser");
            authRequest.setPassword("password123");
        }

        @Test
        @DisplayName("Should authenticate successfully and return JWT token")
        void shouldAuthenticate_WhenValidCredentials() {
            // Given
            String expectedToken = "jwt.token.here";
            when(authRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));
            when(jwtService.generateToken("testuser")).thenReturn(expectedToken);

            // When
            String result = authService.authenticate(authRequest);

            // Then
            assertThat(result).isEqualTo(expectedToken);

            verify(authenticationManager).authenticate(
                    any(UsernamePasswordAuthenticationToken.class));
            verify(authRepository).findByUsername("testuser");
            verify(jwtService).generateToken("testuser");
        }

        @Test
        @DisplayName("Should throw exception when user not found after authentication")
        void shouldThrowException_WhenUserNotFound() {
            // Given
            when(authRepository.findByUsername("testuser")).thenReturn(Optional.empty());

            // When & Then
            assertThrows(RuntimeException.class,
                    () -> authService.authenticate(authRequest));

            verify(authenticationManager).authenticate(any());
            verify(authRepository).findByUsername("testuser");
            verifyNoInteractions(jwtService);
        }


        @Test
        @DisplayName("Should use authentication manager when bypass property is false")
        void shouldUseAuthenticationManager_WhenBypassPropertyFalse() {
            AuthRequestDto authRequest = new AuthRequestDto();
            authRequest.setUsername("testuser");
            authRequest.setPassword("password123");

            User mockUser = new User();
            mockUser.setUsername("testuser");

            String expectedToken = "jwt.token.here";

            when(authRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));
            when(jwtService.generateToken("testuser")).thenReturn(expectedToken);

            String token = authService.authenticate(authRequest);

            assertThat(token).isEqualTo(expectedToken);
            verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
            verify(authRepository).findByUsername("testuser");
            verify(jwtService).generateToken("testuser");
        }

    }
}
