package ru.aston.matveenko_ym.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "Сущность пользователя")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    @Schema(description = "Идентификатор", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    @Schema(description = "Имя", example = "Юрий")
    private String name;
    @Schema(description = "Электронная почта", example = "ym@mail.ru")
    private String email;
    @Schema(description = "Возраст", example = "Целое натуральное число (например, 33)")
    private int age;
    @Schema(description = "Дата добавления")
    private LocalDateTime createdAt;
}