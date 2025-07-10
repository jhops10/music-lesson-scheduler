package com.jhops10.music_lesson_scheduler.dto.lesson;

import com.jhops10.music_lesson_scheduler.model.Lesson;

import java.time.LocalDateTime;

public record LessonResponseDTO(
        Long id,
        LocalDateTime startTime,
        Integer notityBeforeMinutes,
        String studentName,
        String instrument
) {

    public static LessonResponseDTO fromEntity(Lesson lesson) {
        return new LessonResponseDTO(
                lesson.getId(),
                lesson.getStartTime(),
                lesson.getNotifyBeforeMinutes(),
                lesson.getStudent().getStudentName(),
                lesson.getStudent().getInstrument()
        );
    }
}
