package ru.aston.matveenko_ym.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.EntityModel;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Tag(name = "User Controller", description = "Управление пользователями")
@Log4j2
@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Operation(summary = "Вывод всех пользователей", description = "Позволяет получить полный список пользователей")
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<EntityModel<UserDto>>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        List<EntityModel<UserDto>> resources = users.stream()
                .map(userDto -> {
                    EntityModel<UserDto> resource = EntityModel.of(userDto);
                    resource.add(linkTo(methodOn(UserController.class).getUserById(userDto.getId())).withSelfRel());
                    return resource;
                })
                .toList();
        log.info("Fetched all users, count: {}", users.size());
        return ResponseEntity.ok(resources);
    }

    @Operation(summary = "Поиск пользователя по ID", description = "Позволяет найти пользователя по его идентификатору")
    @GetMapping("/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<EntityModel<UserDto>> getUserById(
            @PathVariable @Parameter(description = "ID пользователя") Long id) {
        UserDto userDto = userService.getUserById(id);
        EntityModel<UserDto> resource = EntityModel.of(userDto);
        resource.add(linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel());
        resource.add(linkTo(methodOn(UserController.class).deleteUser(id)).withRel("delete"));
        log.info("Fetched user with ID: {}", id);
        return ResponseEntity.ok(resource);
    }

    @Operation(summary = "Добавление пользователя", description = "Позволяет добавить нового пользователя")
    @PostMapping("/user/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EntityModel<UserDto>> createUser(
            @Valid @RequestBody @Parameter(description = "Данные пользователя") UserDto userDto)
            throws URISyntaxException {
        UserDto createdUser = userService.createUser(userDto);
        EntityModel<UserDto> resource = EntityModel.of(createdUser);
        resource.add(linkTo(methodOn(UserController.class).getUserById(createdUser.getId())).withSelfRel());
        resource.add(linkTo(methodOn(UserController.class).deleteUser(createdUser.getId())).withRel("delete"));
        log.info("User created with ID: {}!", createdUser.getId());
        return ResponseEntity.created(URI.create("/api/v1/users/" + createdUser.getId())).body(resource);
    }

    @Operation(summary = "Редактирование пользователя по ID",
            description = "Позволяет отредактировать существующего пользователя")
    @PutMapping("/user/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<EntityModel<UserDto>> updateUser(
            @PathVariable @Parameter(description = "ID пользователя") Long id,
            @Valid @RequestBody @Parameter(description = "Данные пользователя") UserDto userDto) {
        UserDto updatedUser = userService.updateUser(id, userDto);
        EntityModel<UserDto> resource = EntityModel.of(updatedUser);
        resource.add(linkTo(methodOn(UserController.class).getUserById(updatedUser.getId())).withSelfRel());
        resource.add(linkTo(methodOn(UserController.class).deleteUser(updatedUser.getId())).withRel("delete"));
        log.info("User updated with ID: {}!", id);
        return ResponseEntity.ok(resource);
    }

    @Operation(summary = "Удаление пользователя по ID",
            description = "Позволяет удалить пользователя по его идентификатору")
    @DeleteMapping("/user/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteUser(@PathVariable @Parameter(description = "ID пользователя") Long id) {
        userService.deleteUser(id);
        log.info("User deleted with ID: {}!", id);
        return ResponseEntity.noContent().build();
    }
}