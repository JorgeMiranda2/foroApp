package com.foro.foro.Dtos.Input;

import jakarta.validation.constraints.NotBlank;

public record RegisterDto (
        @NotBlank  String name,
        @NotBlank String email,
        @NotBlank String password
){
}
