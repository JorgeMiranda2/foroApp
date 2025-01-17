package com.foro.foro.Dtos.Input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TopicDto(
        @NotBlank String title,
        @NotBlank String message,
        @NotNull Long courseId) {
}
