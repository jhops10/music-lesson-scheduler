package com.jhops10.music_lesson_scheduler.controller;

import com.jhops10.music_lesson_scheduler.dto.lesson_notification_log.LessonNotificationLogResponseDTO;
import com.jhops10.music_lesson_scheduler.service.LessonNotificationLogService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class LessonNotificationLogController {

    private final LessonNotificationLogService notificationLogService;

    @GetMapping
    public ResponseEntity<List<LessonNotificationLogResponseDTO>> getAll() {
        List<LessonNotificationLogResponseDTO> notifications = notificationLogService.getAll();
        return ResponseEntity.ok().body(notifications);
    }

    @GetMapping("/by-method")
    public ResponseEntity<List<LessonNotificationLogResponseDTO>> getAllByDeliveryMethod(@RequestParam @NotBlank String method) {
        List<LessonNotificationLogResponseDTO> notifications = notificationLogService.getAllByDeliveryMethod(method);
        return ResponseEntity.ok().body(notifications);
    }
}
