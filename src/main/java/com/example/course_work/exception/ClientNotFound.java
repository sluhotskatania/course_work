package com.example.course_work.exception;

import jakarta.persistence.EntityNotFoundException;

public class ClientNotFound extends EntityNotFoundException {
    public ClientNotFound(String message) {
        super(message);
    }
}
