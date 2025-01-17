package com.foro.foro.Dtos.Output;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


public record AccessResponseDto( String token,
                                 String username) {

}
