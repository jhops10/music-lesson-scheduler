package com.jhops10.music_lesson_scheduler.dto.lesson_notification_log;

import com.jhops10.music_lesson_scheduler.model.LessonNotificationLog;

import java.time.LocalDateTime;

public record LessonNotificationLogResponseDTO(
        Long id,
        Long lessonId,
        String studentName,
        LocalDateTime notifiedAt,
        String message,
        String deliveryMethod
) {
    public static LessonNotificationLogResponseDTO fromEntity(LessonNotificationLog notificationLog) {
        return new LessonNotificationLogResponseDTO(
                notificationLog.getId(),
                notificationLog.getLesson().getId(),
                notificationLog.getLesson().getStudent().getStudentName(),
                notificationLog.getNotifiedAt(),
                notificationLog.getMessage(),
                notificationLog.getDeliveryMethod()
        );
    }
}
