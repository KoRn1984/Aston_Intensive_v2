package ru.aston.matveenko_ym.service.Impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.aston.matveenko_ym.config.KafkaProducer;
import ru.aston.matveenko_ym.dto.UserDto;
import ru.aston.matveenko_ym.dto.MessageDto;
import ru.aston.matveenko_ym.dto.convertor.UserMapper;
import ru.aston.matveenko_ym.model.User;
import ru.aston.matveenko_ym.repository.UserRepository;
import ru.aston.matveenko_ym.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final KafkaProducer kafkaProducer;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, KafkaProducer kafkaProducer) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        User savedUser = userRepository.save(user);
        kafkaProducer.sendMessage(new MessageDto(savedUser.getEmail(), "CREATE"));
        return userMapper.toDto(savedUser);
    }

    @Override
    @Transactional
    public UserDto updateUser(Long id, UserDto userDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        existingUser.setName(userDto.getName());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setAge(userDto.getAge());
        User updatedUser = userRepository.save(existingUser);
        return userMapper.toDto(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            kafkaProducer.sendMessage(new MessageDto(user.get().getEmail(), "DELETE"));
            userRepository.deleteById(id);
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }
}