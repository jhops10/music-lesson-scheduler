package com.jhops10.music_lesson_scheduler.integration;


import com.jhops10.music_lesson_scheduler.dto.lesson_notification_log.LessonNotificationLogResponseDTO;
import com.jhops10.music_lesson_scheduler.model.Lesson;
import com.jhops10.music_lesson_scheduler.model.LessonNotificationLog;
import com.jhops10.music_lesson_scheduler.model.Student;
import com.jhops10.music_lesson_scheduler.repository.LessonNotificationLogRepository;
import com.jhops10.music_lesson_scheduler.repository.LessonRepository;
import com.jhops10.music_lesson_scheduler.repository.StudentRepository;
import com.jhops10.music_lesson_scheduler.service.LessonNotificationLogService;
import jakarta.transaction.Transactional;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
@ActiveProfiles("test")
class LessonNotificationLogServiceIntegrationTest {

    @Autowired
    private LessonNotificationLogService lessonNotificationLogService;

    @Autowired
    private LessonNotificationLogRepository lessonNotificationLogRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private StudentRepository studentRepository;

    private final LocalDateTime fixedExampleTime = LocalDateTime.of(2025, 8, 15, 15, 0);

    private Student defaultStudent;
    private Lesson defaultLesson;
    private LessonNotificationLog defaultNotificationLog;

    @BeforeEach
    void setUp() {
        lessonNotificationLogRepository.deleteAll();
        lessonRepository.deleteAll();
        studentRepository.deleteAll();

        defaultStudent = studentRepository.save(
                Instancio.of(Student.class)
                        .ignore(field(Student::getId))
                        .set(field(Student::getLessons), new ArrayList<>())
                        .create()
        );

        defaultLesson = lessonRepository.save(
                Instancio.of(Lesson.class)
                        .ignore(field(Lesson::getId))
                        .set(field(Lesson::getStudent), defaultStudent)
                        .set(field(Lesson::getStartTime), fixedExampleTime)
                        .set(field(Lesson::getNotified), false)
                        .create()
        );

        defaultNotificationLog = lessonNotificationLogRepository.save(
                Instancio.of(LessonNotificationLog.class)
                        .ignore(field(LessonNotificationLog::getId))
                        .set(field(LessonNotificationLog::getLesson), defaultLesson)
                        .set(field(LessonNotificationLog::getNotifiedAt), fixedExampleTime)
                        .create()
        );
    }

    @Test
    void getAll_shouldReturnAllNotificationLogs_whenNotificationLogsExist() {
        List<LessonNotificationLogResponseDTO> result = lessonNotificationLogService.getAll();

        assertThat(result)
                .isNotNull()
                .hasSize(1)
                .allSatisfy(dto -> {
                    assertThat(dto.lessonId()).isEqualTo(defaultLesson.getId());
                    assertThat(dto.studentName()).isEqualTo(defaultStudent.getStudentName());
                });
    }

    @Test
    void getAll_shouldReturnEmptyList_whenNotificationLogsDoNotExist() {
        lessonNotificationLogRepository.deleteAll();

        List<LessonNotificationLogResponseDTO> result = lessonNotificationLogService.getAll();

        assertThat(result)
                .isNotNull()
                .isEmpty();
    }

    @Test
    void getAllByDeliveryMethod_shouldReturnLogs_whenLogsExistForDeliveryMethod() {
        String method = defaultNotificationLog.getDeliveryMethod();

        List<LessonNotificationLogResponseDTO> result = lessonNotificationLogService.getAllByDeliveryMethod(method);

        assertThat(result)
                .isNotNull()
                .hasSize(1)
                .allSatisfy(dto -> {
                    assertThat(dto.deliveryMethod()).isEqualTo(method);
                    assertThat(dto.lessonId()).isEqualTo(defaultLesson.getId());
                });
    }

    @Test
    void getAllByDeliveryMethod_shouldReturnEmptyList_whenLogsDoNotExist() {
        List<LessonNotificationLogResponseDTO> result = lessonNotificationLogService.getAllByDeliveryMethod("inexistente");

        assertThat(result)
                .isNotNull()
                .isEmpty();
    }
}
