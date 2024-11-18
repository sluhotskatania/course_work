package com.example.course_work.exception;

import jakarta.persistence.EntityNotFoundException;

public class TourNotFound extends EntityNotFoundException {
    public TourNotFound(String message) {
        super(message);
    }
}
