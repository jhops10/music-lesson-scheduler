package com.jhops10.music_lesson_scheduler.dto.lesson;

import com.jhops10.music_lesson_scheduler.model.Lesson;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record LessonRequestDTO(
        @NotNull Long studentId,
        @NotNull LocalDateTime startTime,
        @NotNull Integer notifyBeforeMinutes
        ) {
}
