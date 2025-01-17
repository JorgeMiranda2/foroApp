package com.foro.foro.Dtos.Input;


import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


public record LoginDto(
        @NotBlank  String identifier,
        @NotBlank  String password) {


    public String getIdentifier() {
        return identifier;
    }


    public String getPassword() {
        return password;
    }

}
