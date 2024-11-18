package com.example.course_work.exception;

import jakarta.persistence.EntityNotFoundException;

public class AccommodationNotFound extends EntityNotFoundException {
    public AccommodationNotFound(String message) {
        super(message);
    }
}
