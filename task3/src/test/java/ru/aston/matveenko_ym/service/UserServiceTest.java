package ru.aston.matveenko_ym.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.aston.matveenko_ym.dto.UserDto;
import ru.aston.matveenko_ym.dto.convertor.UserMapper;
import ru.aston.matveenko_ym.model.User;
import ru.aston.matveenko_ym.repository.UserRepository;
import ru.aston.matveenko_ym.service.Impl.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setName("Yury Matveenko");
        user.setEmail("korn1984@narod.ru");
        user.setAge(40);
        user.setCreatedAt(LocalDateTime.now());

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("Yury Matveenko");
        userDto.setEmail("korn1984@narod.ru");
        userDto.setAge(40);
        userDto.setCreatedAt(user.getCreatedAt());
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));
        when(userMapper.toDto(user)).thenReturn(userDto);
        List<UserDto> users = userService.getAllUsers();
        assertEquals(1, users.size());
        assertEquals("Yury Matveenko", users.get(0).getName());
    }

    @Test
    void testGetUserById() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDto);
        UserDto foundUser = userService.getUserById(1L);
        assertNotNull(foundUser);
        assertEquals("Yury Matveenko", foundUser.getName());
    }

    @Test
    void testGetUserById_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.getUserById(1L));
        assertEquals("User not found with id: 1", exception.getMessage());
    }

    @Test
    void testCreateUser() {
        when(userMapper.toEntity(userDto)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);
        UserDto createdUser = userService.createUser(userDto);
        assertNotNull(createdUser);
        assertEquals("Yury Matveenko", createdUser.getName());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testUpdateUser() {
        when(userMapper.toEntity(userDto)).thenReturn(user);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        userDto.setName("Bogdan Matveenko");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);
        UserDto updatedUser = userService.updateUser(1L, userDto);
        assertNotNull(updatedUser);
        assertEquals("Bogdan Matveenko", updatedUser.getName());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testUpdateUser_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.updateUser(1L, userDto));
        assertEquals("User not found with id: 1", exception.getMessage());
    }

    @Test
    void testDeleteUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        assertDoesNotThrow(() -> userService.deleteUser(1L));
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.deleteUser(1L));
        assertEquals("User not found with id: 1", exception.getMessage());
    }
}