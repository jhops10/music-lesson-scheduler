package com.jhops10.music_lesson_scheduler.service;

import com.jhops10.music_lesson_scheduler.dto.lesson_notification_log.LessonNotificationLogResponseDTO;
import com.jhops10.music_lesson_scheduler.repository.LessonNotificationLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonNotificationLogService {

    private final LessonNotificationLogRepository notificationLogRepository;

    public List<LessonNotificationLogResponseDTO> getAll() {
        return notificationLogRepository.findAllByOrderByNotifiedAtDesc().stream()
                .map(LessonNotificationLogResponseDTO::fromEntity)
                .toList();
    }

    public List<LessonNotificationLogResponseDTO> getAllByDeliveryMethod(String deliveryMethod) {
        return notificationLogRepository.findByDeliveryMethodIgnoreCase(deliveryMethod).stream()
                .map(LessonNotificationLogResponseDTO::fromEntity)
                .toList();
    }
}
