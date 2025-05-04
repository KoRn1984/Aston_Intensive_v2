package ru.aston.matveenko_ym.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.aston.matveenko_ym.dto.UserDto;
import ru.aston.matveenko_ym.service.Impl.UserServiceImpl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Tag(name = "User Controller", description = "Пользователи")
@Log4j2
@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Operation(summary = "Вывод всех пользователей", description = "Позволяет вывести весь список пользователей")
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    @Operation(summary = "Поиск пользователя по ID", description = "Позволяет найти пользователя по его идентификатору")
    @GetMapping("/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto userDto = userService.getUserById(id);
        return ResponseEntity.ok().body(userDto);
    }

    @Operation(summary = "Добавление пользователя", description = "Позволяет добавить пользователя в сервис")
    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) throws URISyntaxException {
        UserDto createdUser = userService.createUser(userDto);
        log.info("User created!");
        return ResponseEntity.created(URI.create("/api/v1/users/" + createdUser.getId())).body(createdUser);
    }

    @Operation(summary = "Редактирование пользователя по ID", description = "Позволяет отредактировать пользователя в сервисе")
    @PutMapping("/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateUser(id, userDto);
        log.info("User edited!");
        return ResponseEntity.ok().body(updatedUser);
    }

    @Operation(summary = "Удаление пользователя по ID", description = "Позволяет удалить пользователя в сервисе")
    @DeleteMapping("/user/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        log.info("User deleted!");
        return ResponseEntity.noContent().build();
    }
}