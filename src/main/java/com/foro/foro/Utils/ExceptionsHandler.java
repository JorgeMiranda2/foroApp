package com.foro.foro.Utils;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public <T> ResponseEntity<T> handleError404(){
        System.out.println("error 404");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    @ExceptionHandler(DuplicatedTopicException.class)
    public ResponseEntity<String> handleDuplicatedTopicException(DuplicatedTopicException ex) {
        // Aquí se maneja la excepción y se devuelve una respuesta adecuada
        System.out.println("Duplicated Topic Error: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException (Exception ex) {
        // Aquí se maneja la excepción y se devuelve una respuesta adecuada
        System.out.println("Resource not found: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<String> AuthenticationCredentialsNotFoundException (Exception ex) {
        // Aquí se maneja la excepción y se devuelve una respuesta adecuada
        System.out.println("Incorrect credentials: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

}
