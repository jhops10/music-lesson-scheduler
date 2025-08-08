package com.jhops10.music_lesson_scheduler.service;

import com.jhops10.music_lesson_scheduler.dto.lesson.LessonRequestDTO;
import com.jhops10.music_lesson_scheduler.dto.lesson.LessonResponseDTO;
import com.jhops10.music_lesson_scheduler.dto.lesson.LessonUpdateDTO;
import com.jhops10.music_lesson_scheduler.exceptions.LessonNotFoundException;
import com.jhops10.music_lesson_scheduler.exceptions.StudentNotFoundException;
import com.jhops10.music_lesson_scheduler.model.Lesson;
import com.jhops10.music_lesson_scheduler.model.Student;
import com.jhops10.music_lesson_scheduler.repository.LessonRepository;
import com.jhops10.music_lesson_scheduler.repository.StudentRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonServiceTest {

    @InjectMocks
    private LessonService lessonService;

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private StudentRepository studentRepository;

    private final Long defaultId = 1L;
    private final Long nonExistingId = 9999999L;
    private final LocalDateTime fixedStartTime = LocalDateTime.of(2025, 8, 15, 15, 0);
    private final LocalDateTime updatedStartTime = LocalDateTime.of(2025, 9, 20, 14, 0);
    private Student defaultStudent;
    private Lesson defaultLesson;


    @BeforeEach
    void setUp() {
        defaultStudent = createNewStudent(defaultId);
        defaultLesson = createNewLesson(defaultId, fixedStartTime);
    }

    private Student createNewStudent(Long id) {
        return Instancio.of(Student.class)
                .set(field(Student::getId), id)
                .set(field(Student::getLessons), new ArrayList<>())
                .create();
    }

    private Lesson createNewLesson(Long id, LocalDateTime startTime) {
        return Instancio.of(Lesson.class)
                .set(field(Lesson::getId), defaultId)
                .set(field(Lesson::getStartTime), fixedStartTime)
                .set(field(Lesson::getNotified), false)
                .create();
    }

    private LessonResponseDTO expectedLessonResponseDTO() {
        return new LessonResponseDTO(defaultLesson.getId(),
                defaultLesson.getStartTime(),
                defaultLesson.getNotifyBeforeMinutes(),
                defaultLesson.getStudent().getStudentName(),
                defaultLesson.getStudent().getInstrument());
    }

    @Test
    void createLesson_shouldReturnLesson() {
        LessonRequestDTO requestDTO = new LessonRequestDTO(defaultStudent.getId(), defaultLesson.getStartTime(), defaultLesson.getNotifyBeforeMinutes());

        when(studentRepository.findById(defaultId)).thenReturn(Optional.of(defaultStudent));
        when(lessonRepository.save(any(Lesson.class))).thenReturn(defaultLesson);

        LessonResponseDTO sut = lessonService.create(requestDTO);

        assertThat(sut)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedLessonResponseDTO());

        verify(studentRepository).findById(defaultId);
        verify(lessonRepository).save(any(Lesson.class));
        verifyNoMoreInteractions(studentRepository);
        verifyNoMoreInteractions(lessonRepository);
    }

    @Test
    void createLesson_shouldThrowException_whenStudentIdDoesNotExist() {
        LessonRequestDTO requestDTO = new LessonRequestDTO(nonExistingId, defaultLesson.getStartTime(), defaultLesson.getNotifyBeforeMinutes());
        when(studentRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lessonService.create(requestDTO))
                .isInstanceOf(StudentNotFoundException.class);

        verify(studentRepository).findById(nonExistingId);
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    void getAll_shouldReturnAllLessons_whenLessonsExist() {
        when(lessonRepository.findAll()).thenReturn(List.of(defaultLesson));

        List<LessonResponseDTO> sut = lessonService.getAll();

        assertThat(sut)
                .isNotNull()
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(expectedLessonResponseDTO());

        verify(lessonRepository).findAll();
        verifyNoMoreInteractions(lessonRepository);

    }

    @Test
    void getAll_shouldReturnEmptyList_whenLessonsDoNotExist() {
        when(lessonRepository.findAll()).thenReturn(List.of());

        List<LessonResponseDTO> sut = lessonService.getAll();

        assertThat(sut)
                .isNotNull()
                .isEmpty();

        verify(lessonRepository).findAll();
        verifyNoMoreInteractions(lessonRepository);
    }

    @Test
    void getById_shouldReturnLesson_whenIdExists() {
        when(lessonRepository.findById(defaultId)).thenReturn(Optional.of(defaultLesson));

        LessonResponseDTO sut = lessonService.getById(defaultId);

        assertThat(sut)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedLessonResponseDTO());

        verify(lessonRepository).findById(defaultId);
        verifyNoMoreInteractions(lessonRepository);
    }

    @Test
    void getById_shouldThrowException_whenIdDoesNotExist() {
        when(lessonRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lessonService.getById(nonExistingId))
                .isInstanceOf(LessonNotFoundException.class);

        verify(lessonRepository).findById(nonExistingId);
        verifyNoMoreInteractions(lessonRepository);

    }

    @Test
    void updateLesson_shouldReturnUpdatedLesson_whenIdExists() {
        LessonUpdateDTO updateDTO = new LessonUpdateDTO(updatedStartTime, 2);
        Lesson updatedLesson = createNewLesson(defaultLesson.getId(), updateDTO.startTime());

        when(lessonRepository.findById(defaultId)).thenReturn(Optional.of(defaultLesson));
        when(lessonRepository.save(any(Lesson.class))).thenReturn(updatedLesson);

        LessonResponseDTO sut = lessonService.update(defaultId, updateDTO);

        assertThat(sut)
                .isNotNull()
                .extracting(
                        LessonResponseDTO::id,
                        LessonResponseDTO::startTime,
                        LessonResponseDTO::notifyBeforeMinutes
                )
                .containsExactly(
                        updatedLesson.getId(),
                        updatedLesson.getStartTime(),
                        updatedLesson.getNotifyBeforeMinutes()
                );

        verify(lessonRepository).findById(defaultId);
        verify(lessonRepository).save(any(Lesson.class));
        verifyNoMoreInteractions(lessonRepository);

    }

    @Test
    void updateLesson_shouldThrowException_whenIdDoesNotExist() {
        LessonUpdateDTO updateDTO = new LessonUpdateDTO(updatedStartTime, 2);

        when(lessonRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lessonService.update(nonExistingId, updateDTO))
                .isInstanceOf(LessonNotFoundException.class);

        verify(lessonRepository).findById(nonExistingId);
        verifyNoMoreInteractions(lessonRepository);
    }

    @Test
    void deleteLesson_shouldRemoveLesson_whenIdExists() {
        when(lessonRepository.existsById(defaultId)).thenReturn(true);

        lessonService.delete(defaultId);

        verify(lessonRepository).existsById(defaultId);
        verify(lessonRepository).deleteById(defaultId);
        verifyNoMoreInteractions(lessonRepository);

    }

    @Test
    void deleteLesson_shouldThrowException_whenIdDoesNotExist() {
        when(lessonRepository.existsById(nonExistingId)).thenReturn(false);

        assertThatThrownBy(() -> lessonService.delete(nonExistingId))
                .isInstanceOf(LessonNotFoundException.class);

        verify(lessonRepository).existsById(nonExistingId);
        verifyNoMoreInteractions(lessonRepository);
    }


}