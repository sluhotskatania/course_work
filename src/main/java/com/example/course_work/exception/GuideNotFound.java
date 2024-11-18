package com.example.course_work.exception;

import jakarta.persistence.EntityNotFoundException;

public class GuideNotFound extends EntityNotFoundException {
    public GuideNotFound(String message) {
        super(message);
    }
}
