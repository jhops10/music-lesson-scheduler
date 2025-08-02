package com.jhops10.music_lesson_scheduler.service;

import com.jhops10.music_lesson_scheduler.dto.lesson.LessonRequestDTO;
import com.jhops10.music_lesson_scheduler.dto.lesson.LessonResponseDTO;
import com.jhops10.music_lesson_scheduler.exceptions.StudentNotFoundException;
import com.jhops10.music_lesson_scheduler.model.Lesson;
import com.jhops10.music_lesson_scheduler.model.Student;
import com.jhops10.music_lesson_scheduler.repository.LessonRepository;
import com.jhops10.music_lesson_scheduler.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private final LocalDateTime fixedStartTime = LocalDateTime.of(2025, 8, 15, 15,0);
    private Student defaultStudent;
    private List<Lesson> lessons = new ArrayList<>();
    private Lesson defaultLesson;


    @BeforeEach
    void setUp() {
        defaultStudent = createNewStudent(defaultId, "Name Example", "Example Instrument Name", lessons);
        defaultLesson = createNewLesson(defaultId, fixedStartTime, defaultStudent, 1, false);
    }

    private Student createNewStudent(Long id, String studentName, String instrument, List<Lesson> lessons) {
        return Student.builder()
                .id(id)
                .studentName(studentName)
                .instrument(instrument)
                .lessons(lessons)
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

    @Test
    void createLesson_shouldReturnLesson() {
        LessonRequestDTO requestDTO = new LessonRequestDTO(defaultStudent.getId(), defaultLesson.getStartTime(), defaultLesson.getNotifyBeforeMinutes());

        when(studentRepository.findById(defaultId)).thenReturn(Optional.of(defaultStudent));
        when(lessonRepository.save(any(Lesson.class))).thenReturn(defaultLesson);

        LessonResponseDTO sut = lessonService.create(requestDTO);

        assertNotNull(sut);
        assertEquals(defaultLesson.getId(), sut.id());
        assertEquals(defaultLesson.getStudent().getStudentName(), sut.studentName());
        assertEquals(defaultLesson.getStudent().getInstrument(), sut.instrument());
        assertEquals(defaultLesson.getStartTime(), sut.startTime());
        assertEquals(defaultLesson.getNotifyBeforeMinutes(), sut.notityBeforeMinutes());
        assertEquals(defaultLesson.getNotified(), false);

        verify(studentRepository).findById(defaultId);
        verify(lessonRepository).save(any(Lesson.class));
        verifyNoMoreInteractions(studentRepository);
        verifyNoMoreInteractions(lessonRepository);
    }

    @Test
    void createLesson_shouldThrowException_whenStudentIdDoesNotExist() {
        LessonRequestDTO requestDTO = new LessonRequestDTO(nonExistingId, defaultLesson.getStartTime(), defaultLesson.getNotifyBeforeMinutes());
        when(studentRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> lessonService.create(requestDTO));

        verify(studentRepository).findById(nonExistingId);
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    void getAll_shouldReturnAllLessons_whenLessonsExist() {
        when(lessonRepository.findAll()).thenReturn(List.of(defaultLesson));

        List<LessonResponseDTO> sut = lessonService.getAll();

        assertEquals(1, sut.size());
        assertEquals(1, sut.get(0).id());
        assertEquals(fixedStartTime, sut.get(0).startTime());
        assertEquals("Name Example", sut.get(0).studentName());
        assertEquals("Example Instrument Name", sut.get(0).instrument());
        assertEquals(1, sut.get(0).notityBeforeMinutes());

        verify(lessonRepository).findAll();
        verifyNoMoreInteractions(lessonRepository);

    }

    @Test
    void getAll_shouldReturnEmptyList_whenLessonsDosNotExist() {
        when(lessonRepository.findAll()).thenReturn(List.of());

        List<LessonResponseDTO> sut = lessonService.getAll();

        assertNotNull(sut);
        assertTrue(sut.isEmpty());

        verify(lessonRepository).findAll();
        verifyNoMoreInteractions(lessonRepository);
    }
}