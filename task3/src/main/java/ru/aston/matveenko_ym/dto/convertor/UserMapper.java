package ru.aston.matveenko_ym.dto.convertor;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import ru.aston.matveenko_ym.dto.UserDto;
import ru.aston.matveenko_ym.model.User;

@Component
public class UserMapper {

    public UserDto toDto(@NotNull User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setAge(user.getAge());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }

    public User toEntity(@NotNull UserDto dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setAge(dto.getAge());
        return user;
    }
}