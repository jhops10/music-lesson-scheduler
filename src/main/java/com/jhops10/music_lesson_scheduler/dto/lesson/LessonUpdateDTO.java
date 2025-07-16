package com.jhops10.music_lesson_scheduler.dto.lesson;

import com.jhops10.music_lesson_scheduler.model.Lesson;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record LessonUpdateDTO(
        LocalDateTime startTime,
        Integer notifyBeforeMinutes
) {
    public void applyUpdatesTo(Lesson lesson) {
        if (startTime != null) lesson.setStartTime(startTime);
        if (notifyBeforeMinutes != null) lesson.setNotifyBeforeMinutes(notifyBeforeMinutes);
    }
}
