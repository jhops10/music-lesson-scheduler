package com.jhops10.music_lesson_scheduler.integration;

import com.jhops10.music_lesson_scheduler.dto.lesson.LessonRequestDTO;
import com.jhops10.music_lesson_scheduler.dto.lesson.LessonResponseDTO;
import com.jhops10.music_lesson_scheduler.dto.lesson.LessonUpdateDTO;
import com.jhops10.music_lesson_scheduler.exceptions.LessonNotFoundException;
import com.jhops10.music_lesson_scheduler.exceptions.StudentNotFoundException;
import com.jhops10.music_lesson_scheduler.model.Lesson;
import com.jhops10.music_lesson_scheduler.model.Student;
import com.jhops10.music_lesson_scheduler.repository.LessonRepository;
import com.jhops10.music_lesson_scheduler.repository.StudentRepository;
import com.jhops10.music_lesson_scheduler.service.LessonService;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.instancio.Select.field;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
@ActiveProfiles("test")
public class LessonServiceIntegrationTest {

    @Autowired
    private LessonService lessonService;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private StudentRepository studentRepository;

    private final Long defaultId = 1L;
    private final Long nonExistingId = 99999999L;
    private final LocalDateTime updatedStartTime = LocalDateTime.of(2025, 9, 20, 14, 0);
    private Student defaultStudent;
    private Lesson defaultLesson;


    @BeforeEach
    void setUp() {
        cleanDatabase();
        defaultStudent = createNewStudent();
        defaultLesson = createNewLesson(defaultStudent);
        studentRepository.save(defaultStudent);
    }


    private void cleanDatabase() {
        lessonRepository.deleteAll();
        studentRepository.deleteAll();
    }

    private Student createNewStudent() {
        return Instancio.of(Student.class)
                .ignore(field(Student::getId))
                .set(field(Student::getLessons), new ArrayList<>())
                .create();
    }

    private Lesson createNewLesson(Student student) {
        return Instancio.of(Lesson.class)
                .set(field(Lesson::getId), null)
                .set(field(Lesson::getStudent), student)
                .create();
    }

    private LessonRequestDTO createDefaultLessonRequestDTO(Long studentId) {
        return new LessonRequestDTO(
                studentId,
                defaultLesson.getStartTime(),
                defaultLesson.getNotifyBeforeMinutes());
    }

    @Test
    void createLesson_shouldPersistLessonWithStudent_whenStudentExists() {
        LessonRequestDTO requestDTO = createDefaultLessonRequestDTO(defaultStudent.getId());

        LessonResponseDTO savedLesson = lessonService.create(requestDTO);

        Lesson foundLesson = lessonRepository.findById(savedLesson.id())
                .orElseThrow(() -> new AssertionError("Lesson not found"));

        assertThat(foundLesson.getId()).isNotNull();
        assertThat(foundLesson.getStudent()).isNotNull();
        assertThat(foundLesson.getStudent().getId()).isEqualTo(defaultStudent.getId());
    }

    @Test
    void createLesson_shouldThrowException_whenStudentDoesNotExist() {
        LessonRequestDTO requestDTO = createDefaultLessonRequestDTO(nonExistingId);

        assertThatThrownBy(() -> lessonService.create(requestDTO))
                .isInstanceOf(StudentNotFoundException.class);
    }

    @Test
    void getAll_shouldReturnAllLessons_whenLessonsExist() {
        LessonRequestDTO requestDTO = createDefaultLessonRequestDTO(defaultStudent.getId());

        LessonResponseDTO savedLesson = lessonService.create(requestDTO);

        List<LessonResponseDTO> lessons = lessonService.getAll();

        assertThat(lessons)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(savedLesson);
    }

    @Test
    void getAll_shouldReturnEmptyList_whenLessonsDoNotExist() {
        List<LessonResponseDTO> lessons = lessonService.getAll();

        assertThat(lessons)
                .isNotNull()
                .isEmpty();
    }

    @Test
    void getById_shouldReturnLesson_whenIdExists() {
        LessonRequestDTO requestDTO = createDefaultLessonRequestDTO(defaultStudent.getId());

        LessonResponseDTO savedLesson = lessonService.create(requestDTO);

        LessonResponseDTO lesson = lessonService.getById(savedLesson.id());

        assertThat(lesson)
                .usingRecursiveComparison()
                .isEqualTo(savedLesson);
    }

    @Test
    void getById_shouldThrowException_whenIdDoesNotExist() {
        assertThatThrownBy(() -> lessonService.getById(nonExistingId))
                .isInstanceOf(LessonNotFoundException.class);
    }

    @Test
    void updateLesson_shouldReturnUpdatedLesson_whenIdExists() {
        LessonRequestDTO requestDTO = createDefaultLessonRequestDTO(defaultStudent.getId());

        LessonResponseDTO savedLesson = lessonService.create(requestDTO);

        LessonUpdateDTO updateDTO = new LessonUpdateDTO(updatedStartTime, 2);

        LessonResponseDTO updatedLesson = lessonService.update(savedLesson.id(), updateDTO);

        assertThat(updatedLesson.startTime()).isEqualTo(updatedStartTime);
        assertThat(updatedLesson.notifyBeforeMinutes()).isEqualTo(updateDTO.notifyBeforeMinutes());
        assertThat(updatedLesson.studentName()).isEqualTo(savedLesson.studentName());
        assertThat(updatedLesson.instrument()).isEqualTo(savedLesson.instrument());
    }

    @Test
    void updateLesson_shouldThrowException_whenIdDoesNotExist() {
        LessonUpdateDTO updateDTO = new LessonUpdateDTO(updatedStartTime, 2);

        assertThatThrownBy(() -> lessonService.update(nonExistingId, updateDTO))
                .isInstanceOf(LessonNotFoundException.class);
    }

}
