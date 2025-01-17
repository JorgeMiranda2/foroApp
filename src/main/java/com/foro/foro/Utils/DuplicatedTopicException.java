package com.foro.foro.Utils;

public class DuplicatedTopicException extends RuntimeException {

    // Constructor que recibe un mensaje
    public DuplicatedTopicException(String message) {
        super(message);
    }

    // Constructor que recibe un mensaje y una causa (excepción original)
    public DuplicatedTopicException(String message, Throwable cause) {
        super(message, cause);
    }
}