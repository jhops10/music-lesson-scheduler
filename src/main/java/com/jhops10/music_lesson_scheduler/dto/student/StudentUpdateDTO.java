package com.jhops10.music_lesson_scheduler.dto.student;

import com.jhops10.music_lesson_scheduler.model.Student;

public record StudentUpdateDTO(
        String studentName,
        String instrument
) {
    public void applyUpdatesTo(Student entity) {
        if (studentName != null) entity.setStudentName(studentName);
        if (instrument != null) entity.setInstrument(instrument);
    }
}
