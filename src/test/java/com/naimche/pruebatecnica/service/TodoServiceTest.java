package com.naimche.pruebatecnica.service;

import com.naimche.pruebatecnica.dto.TodoDto;
import com.naimche.pruebatecnica.entity.Todo;
import com.naimche.pruebatecnica.entity.User;
import com.naimche.pruebatecnica.exception.auth.NotAuthorizedException;
import com.naimche.pruebatecnica.exception.todo.TodoNotFound;
import com.naimche.pruebatecnica.exception.user.UserNotFound;
import com.naimche.pruebatecnica.mapper.TodoMapper;
import com.naimche.pruebatecnica.repository.TodoRepository;
import com.naimche.pruebatecnica.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@DisplayName("TodoService Tests")
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TodoService todoService;

    // Mocks para simular SecurityContext y Authentication
    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setupSecurityContext() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }
    @BeforeEach
    void setUp() {
        todoService = new TodoService(todoRepository, userRepository);
        ReflectionTestUtils.setField(todoService, "maxTitleLength", 100);
        ReflectionTestUtils.setField(todoService, "maxUsernameLength", 50);
    }
    @Nested
    @DisplayName("findAllTodo tests")
    class FindAllTodoTests {
        @Test
        @DisplayName("Should return paged TodoDto list")
        void shouldReturnPagedTodoDto() {
            Page<Todo> todos = new PageImpl<>(List.of(new Todo()));
            when(todoRepository.findAll(any(PageRequest.class))).thenReturn(todos);

            Page<TodoDto> result = todoService.findAllTodo(PageRequest.of(0, 10));

            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
            verify(todoRepository).findAll(any(PageRequest.class));
        }
    }

    @Nested
    @DisplayName("save tests")
    class SaveTests {
        private User authenticatedUser;
        private User user;
        private TodoDto todoDto;

        @BeforeEach
        void init() {
            authenticatedUser = new User();
            authenticatedUser.setId(1L);
            authenticatedUser.setUsername("authUser");

            user = new User();
            user.setId(1L);

            todoDto = new TodoDto();
            todoDto.setUserId(1L);

            when(authentication.getName()).thenReturn("authUser");
        }

        @Test
        @DisplayName("Should save todo when user authorized")
        void shouldSaveTodoWhenUserAuthorized() {
            when(userRepository.findByUsername("authUser")).thenReturn(Optional.of(authenticatedUser));
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(todoRepository.save(any(Todo.class))).thenAnswer(i -> i.getArgument(0));

            TodoDto saved = todoService.save(todoDto);

            assertThat(saved).isNotNull();
            verify(todoRepository).save(any(Todo.class));
        }

        @Test
        @DisplayName("Should throw NotAuthorizedException when user IDs differ")
        void shouldThrowNotAuthorizedException() {
            user.setId(2L);
            when(userRepository.findByUsername("authUser")).thenReturn(Optional.of(authenticatedUser));
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));

            assertThatThrownBy(() -> todoService.save(todoDto))
                    .isInstanceOf(NotAuthorizedException.class);

            verify(todoRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw UserNotFound when authenticated user missing")
        void shouldThrowUserNotFoundForAuthenticatedUser() {
            when(userRepository.findByUsername("authUser")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> todoService.save(todoDto))
                    .isInstanceOf(UserNotFound.class);

            verify(todoRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw UserNotFound when user by id missing")
        void shouldThrowUserNotFoundForUserId() {
            when(userRepository.findByUsername("authUser")).thenReturn(Optional.of(authenticatedUser));
            when(userRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> todoService.save(todoDto))
                    .isInstanceOf(UserNotFound.class);

            verify(todoRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("saveWithoutAuth tests")
    class SaveWithoutAuthTests {
        @Test
        @DisplayName("Should save todo without authentication")
        void shouldSaveWithoutAuth() {
            User user = new User();
            user.setId(1L);

            TodoDto todoDto = new TodoDto();
            todoDto.setUserId(1L);

            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(todoRepository.save(any(Todo.class))).thenAnswer(i -> i.getArgument(0));

            TodoDto saved = todoService.saveWithoutAuth(todoDto);

            assertThat(saved).isNotNull();
            verify(todoRepository).save(any(Todo.class));
        }

        @Test
        @DisplayName("Should throw UserNotFound if user not found")
        void shouldThrowUserNotFound() {
            TodoDto todoDto = new TodoDto();
            todoDto.setUserId(1L);

            when(userRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> todoService.saveWithoutAuth(todoDto))
                    .isInstanceOf(UserNotFound.class);

            verify(todoRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("findAllTodoFiltered tests")
    class FindAllTodoFilteredTests {
        @Test
        @DisplayName("Should throw IllegalArgumentException on malicious title")
        void shouldThrowExceptionOnMaliciousTitle() {
            assertThatThrownBy(() -> todoService.findAllTodoFiltered(PageRequest.of(0, 10), "<script>", null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException on too long title")
        void shouldThrowExceptionOnTooLongTitle() {
            String longTitle = "a".repeat(101);
            assertThatThrownBy(() -> todoService.findAllTodoFiltered(PageRequest.of(0, 10), longTitle, null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException on malicious username")
        void shouldThrowExceptionOnMaliciousUsername() {
            assertThatThrownBy(() -> todoService.findAllTodoFiltered(PageRequest.of(0, 10), null, "<script>"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException on too long username")
        void shouldThrowExceptionOnTooLongUsername() {
            String longUsername = "a".repeat(51);
            assertThatThrownBy(() -> todoService.findAllTodoFiltered(PageRequest.of(0, 10), null, longUsername))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Should return filtered todos")
        void shouldReturnFilteredTodos() {
            Page<Todo> todos = new PageImpl<>(List.of(new Todo()));
            when(todoRepository.findAllFiltered(any(), any(), any())).thenReturn(todos);

            Page<TodoDto> result = todoService.findAllTodoFiltered(PageRequest.of(0, 10), "title", "username");

            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
            verify(todoRepository).findAllFiltered("title", "username", PageRequest.of(0, 10));
        }
    }

    @Nested
    @DisplayName("findById tests")
    class FindByIdTests {
        @Test
        @DisplayName("Should return todoDto when found")
        void shouldReturnTodoDto() {
            Todo todo = new Todo();
            when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

            TodoDto result = todoService.findById(1L);

            assertThat(result).isNotNull();
            verify(todoRepository).findById(1L);
        }

        @Test
        @DisplayName("Should throw TodoNotFound when not found")
        void shouldThrowTodoNotFound() {
            when(todoRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> todoService.findById(1L))
                    .isInstanceOf(TodoNotFound.class);
        }
    }

    @Nested
    @DisplayName("delete tests")
    class DeleteTests {
        private User authenticatedUser;
        private Todo todo;

        @BeforeEach
        void init() {
            authenticatedUser = new User();
            authenticatedUser.setId(1L);
            authenticatedUser.setUsername("authUser");

            User todoUser = new User();
            todoUser.setId(1L);

            todo = new Todo();
            todo.setId(1L);
            todo.setUser(todoUser);

            when(authentication.getName()).thenReturn("authUser");
        }

        @Test
        @DisplayName("Should delete todo when authorized")
        void shouldDeleteTodo() {
            when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));
            when(userRepository.findByUsername("authUser")).thenReturn(Optional.of(authenticatedUser));

            boolean deleted = todoService.delete(1L);

            assertThat(deleted).isTrue();
            verify(todoRepository).delete(todo);
        }

        @Test
        @DisplayName("Should throw NotAuthorizedException when user not authorized")
        void shouldThrowNotAuthorizedException() {
            User otherUser = new User();
            otherUser.setId(2L);
            todo.getUser().setId(2L);

            when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));
            when(userRepository.findByUsername("authUser")).thenReturn(Optional.of(authenticatedUser));

            assertThatThrownBy(() -> todoService.delete(1L))
                    .isInstanceOf(NotAuthorizedException.class);

            verify(todoRepository, never()).delete(any());
        }

        @Test
        @DisplayName("Should throw TodoNotFound when todo missing")
        void shouldThrowTodoNotFound() {
            when(todoRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> todoService.delete(1L))
                    .isInstanceOf(TodoNotFound.class);
        }

        @Test
        @DisplayName("Should throw UserNotFound when user missing")
        void shouldThrowUserNotFound() {
            when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));
            when(userRepository.findByUsername("authUser")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> todoService.delete(1L))
                    .isInstanceOf(UserNotFound.class);

            verify(todoRepository, never()).delete(any());
        }
    }
}
