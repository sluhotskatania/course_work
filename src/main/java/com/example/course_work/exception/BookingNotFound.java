package com.example.course_work.exception;

import jakarta.persistence.EntityNotFoundException;

public class BookingNotFound extends EntityNotFoundException {
    public BookingNotFound(String message) {
        super(message);
    }
}
