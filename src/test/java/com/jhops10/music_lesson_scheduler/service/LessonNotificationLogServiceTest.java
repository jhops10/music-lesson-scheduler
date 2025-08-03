package com.jhops10.music_lesson_scheduler.service;

import com.jhops10.music_lesson_scheduler.dto.lesson_notification_log.LessonNotificationLogResponseDTO;
import com.jhops10.music_lesson_scheduler.model.Lesson;
import com.jhops10.music_lesson_scheduler.model.LessonNotificationLog;
import com.jhops10.music_lesson_scheduler.model.Student;
import com.jhops10.music_lesson_scheduler.repository.LessonNotificationLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonNotificationLogServiceTest {

    @InjectMocks
    private LessonNotificationLogService lessonNotificationLogService;

    @Mock
    private LessonNotificationLogRepository lessonNotificationLogRepository;

    private final Long defaultId = 1L;
    private final LocalDateTime fixedExampleTime = LocalDateTime.of(2025, 8, 15, 15,0);

    private LessonNotificationLog defaultNotificationLog;
    private Lesson defaultLesson;
    private Student defaultStudent;
    private List<Lesson> lessons = new ArrayList<>();

    @BeforeEach
    void setUp() {
        defaultStudent = createNewStudent(defaultId, "Name Example", "Example Instrument Name", lessons);
        defaultLesson = createNewLesson(defaultId, fixedExampleTime, defaultStudent, 1, false);
        defaultNotificationLog = createNewNotificationLog(defaultId, defaultLesson, fixedExampleTime, "Message", "Method");
    }

    private LessonNotificationLog createNewNotificationLog(Long id, Lesson lesson, LocalDateTime notifiedAt, String message, String deliveryMethod) {
        return LessonNotificationLog.builder()
                .id(id)
                .lesson(lesson)
                .notifiedAt(notifiedAt)
                .message(message)
                .deliveryMethod(deliveryMethod)
                .build();
    }

    private Lesson createNewLesson(Long id, LocalDateTime startTime, Student student, Integer notifyBeforeMinutes, Boolean notified) {
        return Lesson.builder()
                .id(id)
                .startTime(startTime)
                .student(student)
                .notifyBeforeMinutes(notifyBeforeMinutes)
                .notified(notified)
                .build();
    }

    private Student createNewStudent(Long id, String studentName, String instrument, List<Lesson> lessons) {
        return Student.builder()
                .id(id)
                .studentName(studentName)
                .instrument(instrument)
                .lessons(lessons)
                .build();
    }

    @Test
    void getAll_shouldReturnAllNotificationLogs_whenNotificationLogsExist() {
        when(lessonNotificationLogRepository.findAllByOrderByNotifiedAtDesc()).thenReturn(List.of(defaultNotificationLog));

        List<LessonNotificationLogResponseDTO> sut = lessonNotificationLogService.getAll();

        assertEquals(1, sut.size());
        assertEquals(defaultId, sut.get(0).id());
        assertEquals(defaultId, sut.get(0).lessonId());
        assertEquals(defaultNotificationLog.getNotifiedAt(), sut.get(0).notifiedAt());
        assertEquals(defaultNotificationLog.getMessage(), sut.get(0).message());
        assertEquals(defaultNotificationLog.getDeliveryMethod(), sut.get(0).deliveryMethod());

        verify(lessonNotificationLogRepository).findAllByOrderByNotifiedAtDesc();
        verifyNoMoreInteractions(lessonNotificationLogRepository);

    }

    @Test
    void getAll_shouldReturnEmptyList_whenNotificationLogsDoNotExist() {
        when(lessonNotificationLogRepository.findAllByOrderByNotifiedAtDesc()).thenReturn(List.of());

        List<LessonNotificationLogResponseDTO> sut = lessonNotificationLogService.getAll();

        assertNotNull(sut);
        assertTrue(sut.isEmpty());

        verify(lessonNotificationLogRepository).findAllByOrderByNotifiedAtDesc();
        verifyNoMoreInteractions(lessonNotificationLogRepository);
    }

    @Test
    void getAllByDeliveryMethod_shouldReturnLogs_whenLogsExistForDeliveryMethod() {
        when(lessonNotificationLogRepository.findByDeliveryMethodIgnoreCase("method")).thenReturn(List.of(defaultNotificationLog));

        List<LessonNotificationLogResponseDTO> sut = lessonNotificationLogService.getAllByDeliveryMethod("method");

        assertEquals(1, sut.size());
        assertEquals(defaultId, sut.get(0).id());
        assertEquals(defaultId, sut.get(0).lessonId());
        assertEquals(defaultNotificationLog.getNotifiedAt(), sut.get(0).notifiedAt());
        assertEquals(defaultNotificationLog.getMessage(), sut.get(0).message());
        assertEquals(defaultNotificationLog.getDeliveryMethod(), sut.get(0).deliveryMethod());

        verify(lessonNotificationLogRepository).findByDeliveryMethodIgnoreCase("method");
        verifyNoMoreInteractions(lessonNotificationLogRepository);
    }

    @Test
    void getAllByDeliveryMethod_shouldReturnEmptyList_whenLogsDoNotExist() {
        when(lessonNotificationLogRepository.findByDeliveryMethodIgnoreCase("method")).thenReturn(List.of());

        List<LessonNotificationLogResponseDTO> sut = lessonNotificationLogService.getAllByDeliveryMethod("method");

        assertNotNull(sut);
        assertTrue(sut.isEmpty());

        verify(lessonNotificationLogRepository).findByDeliveryMethodIgnoreCase("method");
        verifyNoMoreInteractions(lessonNotificationLogRepository);
    }
}