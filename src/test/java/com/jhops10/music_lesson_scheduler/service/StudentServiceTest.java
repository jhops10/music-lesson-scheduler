package com.jhops10.music_lesson_scheduler.service;

import com.jhops10.music_lesson_scheduler.dto.student.StudentRequestDTO;
import com.jhops10.music_lesson_scheduler.dto.student.StudentResponseDTO;
import com.jhops10.music_lesson_scheduler.dto.student.StudentUpdateDTO;
import com.jhops10.music_lesson_scheduler.exceptions.StudentNotFoundException;
import com.jhops10.music_lesson_scheduler.model.Student;
import com.jhops10.music_lesson_scheduler.repository.StudentRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.instancio.Select.field;
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

    @BeforeEach
    void setUp() {
        defaultStudent = createNewStudent(defaultId);
    }

    private Student createNewStudent(Long id) {
        return Instancio.of(Student.class)
                .set(field(Student::getId), id)
                .set(field(Student::getLessons), new ArrayList<>())
                .create();
    }

    private StudentResponseDTO expectedStudentResponseDTO() {
        return new StudentResponseDTO(
                defaultStudent.getId(),
                defaultStudent.getStudentName(),
                defaultStudent.getInstrument()
        );
    }

    @Test
    void createStudent_shouldReturnStudent() {
        StudentRequestDTO requestDTO = new StudentRequestDTO(defaultStudent.getStudentName(), defaultStudent.getInstrument());

        when(studentRepository.save(any(Student.class))).thenReturn(defaultStudent);

        StudentResponseDTO sut = studentService.create(requestDTO);

        assertThat(sut)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedStudentResponseDTO());

        verify(studentRepository).save(any(Student.class));
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    void getAllStudents_shouldReturnAllStudents_whenStudentsExist() {
        when(studentRepository.findAll()).thenReturn(List.of(defaultStudent));

        List<StudentResponseDTO> sut = studentService.getAll();

        assertThat(sut)
                .isNotNull()
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(expectedStudentResponseDTO());

        verify(studentRepository).findAll();
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    void getAllStudents_shouldReturnEmptyList_whenStudentsDoNotExist() {
        when(studentRepository.findAll()).thenReturn(List.of());

        List<StudentResponseDTO> sut = studentService.getAll();

        assertThat(sut)
                .isNotNull()
                .isEmpty();

        verify(studentRepository).findAll();
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    void getStudentById_shouldReturnStudent_whenIdExists() {
        when(studentRepository.findById(defaultId)).thenReturn(Optional.of(defaultStudent));

        StudentResponseDTO sut = studentService.getById(defaultId);

        assertThat(sut)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedStudentResponseDTO());

        verify(studentRepository).findById(defaultId);
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    void getStudentById_shouldThrowException_whenIdDoesNotExist() {
        when(studentRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.getById(nonExistingId))
                .isInstanceOf(StudentNotFoundException.class);

        verify(studentRepository).findById(nonExistingId);
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    void updateStudent_shouldReturnUpdatedStudent_whenIdExists() {
        StudentUpdateDTO updateDTO = Instancio.of(StudentUpdateDTO.class).create();
        Student updatedStudent = createNewStudent(defaultStudent.getId());

        when(studentRepository.findById(defaultId)).thenReturn(Optional.of(defaultStudent));
        when(studentRepository.save(any(Student.class))).thenReturn(updatedStudent);

        StudentResponseDTO sut = studentService.update(defaultStudent.getId(), updateDTO);

        assertThat(sut)
                .isNotNull()
                .extracting(
                        StudentResponseDTO::id,
                        StudentResponseDTO::studentName,
                        StudentResponseDTO::instrument
                )
                .containsExactly(
                        updatedStudent.getId(),
                        updatedStudent.getStudentName(),
                        updatedStudent.getInstrument()
                );

        verify(studentRepository).findById(defaultId);
        verify(studentRepository).save(defaultStudent);
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    void updateStudent_shouldThrowException_whenIdDoesNotExist() {
        StudentUpdateDTO updateDTO = Instancio.of(StudentUpdateDTO.class).create();

        when(studentRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.update(nonExistingId, updateDTO))
                .isInstanceOf(StudentNotFoundException.class);

        verify(studentRepository).findById(nonExistingId);
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    void deleteStudent_shouldRemoveStudent_whenIdExists() {
        when(studentRepository.existsById(defaultId)).thenReturn(true);

        studentService.delete(defaultId);

        verify(studentRepository).existsById(defaultId);
        verify(studentRepository).deleteById(defaultId);
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    void deleteStudent_shouldThrowException_whenIdDoesNotExist() {
        when(studentRepository.existsById(nonExistingId)).thenReturn(false);

        assertThatThrownBy(() -> studentService.delete(nonExistingId))
                .isInstanceOf(StudentNotFoundException.class);

        verify(studentRepository).existsById(nonExistingId);
        verifyNoMoreInteractions(studentRepository);
    }

}


