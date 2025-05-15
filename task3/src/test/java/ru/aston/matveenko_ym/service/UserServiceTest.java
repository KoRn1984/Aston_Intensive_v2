package ru.aston.matveenko_ym.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;
import ru.aston.matveenko_ym.config.KafkaProducer;
import ru.aston.matveenko_ym.dto.MessageDto;
import ru.aston.matveenko_ym.dto.UserDto;
import ru.aston.matveenko_ym.dto.convertor.UserMapper;
import ru.aston.matveenko_ym.model.User;
import ru.aston.matveenko_ym.repository.UserRepository;
import ru.aston.matveenko_ym.service.Impl.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private KafkaProducer kafkaProducer;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

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
    @Transactional
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));
        when(userMapper.toDto(user)).thenReturn(userDto);
        List<UserDto> users = userServiceImpl.getAllUsers();
        assertEquals(1, users.size());
        assertEquals("Yury Matveenko", users.get(0).getName());
    }

    @Test
    @Transactional
    void testGetUserById() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDto);
        UserDto foundUser = userServiceImpl.getUserById(1L);
        assertNotNull(foundUser);
        assertEquals("Yury Matveenko", foundUser.getName());
    }

    @Test
    @Transactional
    void testGetUserById_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userServiceImpl.getUserById(1L));
        assertEquals("User not found with id: 1", exception.getMessage());
    }

    @Test
    @Transactional
    public void testCreateUser() {
        User savedUser = new User();
        savedUser.setId(1L);
        when(userMapper.toEntity(userDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userMapper.toDto(savedUser)).thenReturn(userDto);
        UserDto result = userServiceImpl.createUser(userDto);
        assertNotNull(result);
        assertEquals(userDto.getEmail(), result.getEmail());
        verify(userMapper, times(1)).toEntity(userDto);
        verify(userRepository, times(1)).save(user);
        verify(kafkaProducer, times(1)).sendMessage(any(MessageDto.class));
    }

    @Test
    @Transactional
    void testUpdateUser() {
        when(userMapper.toEntity(userDto)).thenReturn(user);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        userDto.setName("Bogdan Matveenko");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);
        UserDto updatedUser = userServiceImpl.updateUser(1L, userDto);
        assertNotNull(updatedUser);
        assertEquals("Bogdan Matveenko", updatedUser.getName());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @Transactional
    void testUpdateUser_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userServiceImpl.updateUser(1L, userDto));
        assertEquals("User not found with id: 1", exception.getMessage());
    }

    @Test
    @Transactional
    public void testDeleteUser_Success() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        userServiceImpl.deleteUser(userId);
        verify(kafkaProducer, times(1)).sendMessage(new MessageDto(user.getEmail(), "DELETE"));
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void testDeleteUser_UserNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userServiceImpl.deleteUser(userId);
        });
        assertEquals("User not found with id: " + userId, exception.getMessage());
        verify(kafkaProducer, never()).sendMessage(any());
        verify(userRepository, never()).deleteById(anyLong());
    }
}