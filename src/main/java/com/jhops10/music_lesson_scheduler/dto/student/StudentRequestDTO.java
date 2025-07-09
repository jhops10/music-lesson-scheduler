package com.jhops10.music_lesson_scheduler.dto.student;

import com.jhops10.music_lesson_scheduler.model.Student;
import jakarta.validation.constraints.NotBlank;

public record StudentRequestDTO(
        @NotBlank(message = "O campo nome é obrigatório.")
        String studentName,
        @NotBlank(message = "O campo instrumento é obrigatório.")
        String instrument
) {
        public Student toEntity() {
                return Student.builder()
                        .studentName(studentName)
                        .instrument(instrument)
                        .build();
        }
}
