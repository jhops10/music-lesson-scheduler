package com.jhops10.music_lesson_scheduler.dto.student;

import com.jhops10.music_lesson_scheduler.model.Student;

public record StudentResponseDTO(
        Long id,
        String studentName,
        String instrument
) {
    public static StudentResponseDTO fromEntity(Student student) {
        return new StudentResponseDTO(
                student.getId(),
                student.getStudentName(),
                student.getInstrument()
        );
    }

}
