package com.jhops10.music_lesson_scheduler.exceptions;

public class LessonNotFoundException extends RuntimeException {
    public LessonNotFoundException(String message) {
        super(message);
    }
}
