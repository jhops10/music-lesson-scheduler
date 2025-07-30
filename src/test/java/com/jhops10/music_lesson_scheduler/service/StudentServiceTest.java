package com.jhops10.music_lesson_scheduler.service;

import com.jhops10.music_lesson_scheduler.dto.student.StudentRequestDTO;
import com.jhops10.music_lesson_scheduler.dto.student.StudentResponseDTO;
import com.jhops10.music_lesson_scheduler.dto.student.StudentUpdateDTO;
import com.jhops10.music_lesson_scheduler.exceptions.StudentNotFoundException;
import com.jhops10.music_lesson_scheduler.model.Lesson;
import com.jhops10.music_lesson_scheduler.model.Student;
import com.jhops10.music_lesson_scheduler.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @InjectMocks
    private StudentService studentService;

    @Mock
    private StudentRepository studentRepository;


    private final Long nonExistingId = 9999999L;
    private final Long defaultId = 1L;
    private Student defaultStudent;
    private List<Lesson> lessons = new ArrayList<>();

    @BeforeEach
    void setUp() {
        defaultStudent = createNewStudent(defaultId, "Name Example", "Example Instrument Name", lessons);
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
    void createStudent_shouldReturnStudent() {
        StudentRequestDTO requestDTO = new StudentRequestDTO(defaultStudent.getStudentName(),defaultStudent.getInstrument());

        when(studentRepository.save(any(Student.class))).thenReturn(defaultStudent);

        StudentResponseDTO sut = studentService.create(requestDTO);

        assertNotNull(sut);
        assertEquals(defaultStudent.getId(), sut.id());
        assertEquals(defaultStudent.getStudentName(), sut.studentName());
        assertEquals(defaultStudent.getInstrument(), sut.instrument());
        assertTrue(defaultStudent.getLessons().isEmpty());

        verify(studentRepository).save(any(Student.class));
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    void getAllStudents_shouldReturnAllStudents_whenStudentsExist() {
        when(studentRepository.findAll()).thenReturn(List.of(defaultStudent));

        List<StudentResponseDTO> sut = studentService.getAll();

        assertEquals(1, sut.size());
        assertEquals(1, sut.get(0).id());
        assertEquals("Name Example", sut.get(0).studentName());
        assertEquals("Example Instrument Name", sut.get(0).instrument());

        verify(studentRepository).findAll();
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    void getAllStudents_shouldReturnEmptyList_whenStudentsDoNotExist() {
        when(studentRepository.findAll()).thenReturn(List.of());

        List<StudentResponseDTO> sut = studentService.getAll();

        assertNotNull(sut);
        assertTrue(sut.isEmpty());

        verify(studentRepository).findAll();
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    void getStudentById_shouldReturnStudent_whenIdExists() {
        when(studentRepository.findById(defaultId)).thenReturn(Optional.of(defaultStudent));

        StudentResponseDTO sut = studentService.getById(defaultId);

        assertNotNull(sut);
        assertEquals(defaultId, sut.id());
        assertEquals("Name Example", sut.studentName());
        assertEquals("Example Instrument Name", sut.instrument());

        verify(studentRepository).findById(defaultId);
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    void getStudentById_shouldThrowException_whenIdDoesNotExist() {
        when(studentRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> studentService.getById(nonExistingId));

        verify(studentRepository).findById(nonExistingId);
        verifyNoMoreInteractions(studentRepository);


    }

    @Test
    void updateStudent_shouldReturnUpdatedStudent_whenIdExists() {
        StudentUpdateDTO updateDTO = new StudentUpdateDTO("Updated Name", "Updated Instrument Name");
        Student updatedStudent = createNewStudent(defaultStudent.getId(), updateDTO.studentName(), updateDTO.instrument(), defaultStudent.getLessons());

        when(studentRepository.findById(defaultId)).thenReturn(Optional.of(defaultStudent));
        when(studentRepository.save(any(Student.class))).thenReturn(updatedStudent);

        StudentResponseDTO sut = studentService.update(defaultStudent.getId(), updateDTO);

        assertNotNull(sut);
        assertEquals(updatedStudent.getId(), sut.id());
        assertEquals(updatedStudent.getStudentName(), sut.studentName());
        assertEquals(updatedStudent.getInstrument(), sut.instrument());

        verify(studentRepository).findById(defaultId);
        verify(studentRepository).save(defaultStudent);
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    void updateStudent_shouldThrowException_whenIdDoesNotExist() {
        StudentUpdateDTO updateDTO = new StudentUpdateDTO("Updated Name", "Updated Instrument Name");

        when(studentRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> studentService.update(nonExistingId, updateDTO));

        verify(studentRepository).findById(nonExistingId);
        verifyNoMoreInteractions(studentRepository);

    }

}


