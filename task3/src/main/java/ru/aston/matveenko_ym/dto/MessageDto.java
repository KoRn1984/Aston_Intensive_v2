package ru.aston.matveenko_ym.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Сущность сообщения")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDto {
    @Schema(description = "Электронная почта", example = "ym@mail.ru")
    private String email;
    @Schema(description = "Операция создания или удаления пользователя", example = "CREATE или DELETE")
    private String operation;
}