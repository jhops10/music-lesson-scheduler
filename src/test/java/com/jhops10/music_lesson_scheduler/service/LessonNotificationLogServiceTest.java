package com.jhops10.music_lesson_scheduler.service;

import com.jhops10.music_lesson_scheduler.dto.lesson_notification_log.LessonNotificationLogResponseDTO;
import com.jhops10.music_lesson_scheduler.model.Lesson;
import com.jhops10.music_lesson_scheduler.model.LessonNotificationLog;
import com.jhops10.music_lesson_scheduler.model.Student;
import com.jhops10.music_lesson_scheduler.repository.LessonNotificationLogRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonNotificationLogServiceTest {

    @InjectMocks
    private LessonNotificationLogService lessonNotificationLogService;

    @Mock
    private LessonNotificationLogRepository lessonNotificationLogRepository;

    private final Long defaultId = 1L;
    private final LocalDateTime fixedExampleTime = LocalDateTime.of(2025, 8, 15, 15, 0);

    private LessonNotificationLog defaultNotificationLog;
    private Lesson defaultLesson;
    private Student defaultStudent;

    @BeforeEach
    void setUp() {
        defaultStudent = createNewStudent(defaultId);
        defaultLesson = createNewLesson(defaultId, fixedExampleTime);
        defaultNotificationLog = createNewNotificationLog(defaultId, defaultLesson);
    }

    private LessonNotificationLog createNewNotificationLog(Long id, Lesson lesson) {
        return Instancio.of(LessonNotificationLog.class)
                .set(field(LessonNotificationLog::getId), defaultId)
                .set(field(LessonNotificationLog::getLesson), lesson)
                .set(field(LessonNotificationLog::getNotifiedAt), fixedExampleTime)
                .create();

    }

    private Lesson createNewLesson(Long id, LocalDateTime startTime) {
        return Instancio.of(Lesson.class)
                .set(field(Lesson::getId), defaultId)
                .set(field(Lesson::getStartTime), fixedExampleTime)
                .set(field(Lesson::getNotified), false)
                .create();
    }

    private Student createNewStudent(Long id) {
        return Instancio.of(Student.class)
                .set(field(Student::getId), id)
                .set(field(Student::getLessons), new ArrayList<>())
                .create();
    }

    private LessonNotificationLogResponseDTO expectedLessonLogNotificationDTO() {
        return new LessonNotificationLogResponseDTO(
                defaultNotificationLog.getId(),
                defaultNotificationLog.getLesson().getId(),
                defaultNotificationLog.getLesson().getStudent().getStudentName(),
                defaultNotificationLog.getNotifiedAt(),
                defaultNotificationLog.getMessage(),
                defaultNotificationLog.getDeliveryMethod()
        );
    }

    @Test
    void getAll_shouldReturnAllNotificationLogs_whenNotificationLogsExist() {
        when(lessonNotificationLogRepository.findAllByOrderByNotifiedAtDesc()).thenReturn(List.of(defaultNotificationLog));

        List<LessonNotificationLogResponseDTO> sut = lessonNotificationLogService.getAll();

        assertThat(sut)
                .isNotNull()
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(expectedLessonLogNotificationDTO());

        verify(lessonNotificationLogRepository).findAllByOrderByNotifiedAtDesc();
        verifyNoMoreInteractions(lessonNotificationLogRepository);

    }

    @Test
    void getAll_shouldReturnEmptyList_whenNotificationLogsDoNotExist() {
        when(lessonNotificationLogRepository.findAllByOrderByNotifiedAtDesc()).thenReturn(List.of());

        List<LessonNotificationLogResponseDTO> sut = lessonNotificationLogService.getAll();

        assertThat(sut)
                .isNotNull()
                .isEmpty();

        verify(lessonNotificationLogRepository).findAllByOrderByNotifiedAtDesc();
        verifyNoMoreInteractions(lessonNotificationLogRepository);
    }

    @Test
    void getAllByDeliveryMethod_shouldReturnLogs_whenLogsExistForDeliveryMethod() {
        when(lessonNotificationLogRepository.findByDeliveryMethodIgnoreCase("method")).thenReturn(List.of(defaultNotificationLog));

        List<LessonNotificationLogResponseDTO> sut = lessonNotificationLogService.getAllByDeliveryMethod("method");

        assertThat(sut)
                .isNotNull()
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(expectedLessonLogNotificationDTO());

        verify(lessonNotificationLogRepository).findByDeliveryMethodIgnoreCase("method");
        verifyNoMoreInteractions(lessonNotificationLogRepository);
    }

    @Test
    void getAllByDeliveryMethod_shouldReturnEmptyList_whenLogsDoNotExist() {
        when(lessonNotificationLogRepository.findByDeliveryMethodIgnoreCase("method")).thenReturn(List.of());

        List<LessonNotificationLogResponseDTO> sut = lessonNotificationLogService.getAllByDeliveryMethod("method");

        assertThat(sut)
                .isNotNull()
                .isEmpty();

        verify(lessonNotificationLogRepository).findByDeliveryMethodIgnoreCase("method");
        verifyNoMoreInteractions(lessonNotificationLogRepository);
    }
}