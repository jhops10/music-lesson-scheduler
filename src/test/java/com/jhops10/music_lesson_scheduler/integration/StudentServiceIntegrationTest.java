package com.jhops10.music_lesson_scheduler.integration;

import com.jhops10.music_lesson_scheduler.dto.student.StudentRequestDTO;
import com.jhops10.music_lesson_scheduler.dto.student.StudentResponseDTO;
import com.jhops10.music_lesson_scheduler.dto.student.StudentUpdateDTO;
import com.jhops10.music_lesson_scheduler.exceptions.StudentNotFoundException;
import com.jhops10.music_lesson_scheduler.model.Student;
import com.jhops10.music_lesson_scheduler.repository.StudentRepository;
import com.jhops10.music_lesson_scheduler.service.StudentService;
import jakarta.transaction.Transactional;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.instancio.Select.field;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
@ActiveProfiles("test")
public class StudentServiceIntegrationTest {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    private Student defaultStudent;
    private final Long nonExistingId = 9999999L;

    @BeforeEach
    void setUp() {
        cleanDatabase();
        defaultStudent = createNewStudent();
    }

    private void cleanDatabase() {
        studentRepository.deleteAll();
    }

    private Student createNewStudent() {
        return Instancio.of(Student.class)
                .ignore(field(Student::getId))
                .set(field(Student::getLessons), new ArrayList<>())
                .create();
    }

    private StudentRequestDTO createDefaultStudentRequestDTO() {
        return new StudentRequestDTO(
                defaultStudent.getStudentName(),
                defaultStudent.getInstrument()
        );
    }

    @Test
    void createStudent_shouldReturnStudent() {
        StudentRequestDTO requestDTO = createDefaultStudentRequestDTO();

        StudentResponseDTO savedStudent = studentService.create(requestDTO);

        Student foundStudent = studentRepository.findById(savedStudent.id())
                .orElseThrow(() -> new AssertionError("Lesson not found"));

        assertThat(foundStudent.getId()).isNotNull();
        assertThat(foundStudent.getStudentName()).isEqualTo(defaultStudent.getStudentName());
        assertThat(foundStudent.getInstrument()).isEqualTo(defaultStudent.getInstrument());
    }

    @Test
    void getAll_shouldReturnAllStudents_whenStudentsExist() {
        StudentRequestDTO requestDTO = createDefaultStudentRequestDTO();

        StudentResponseDTO savedStudent = studentService.create(requestDTO);

        List<StudentResponseDTO> students = studentService.getAll();

        assertThat(students)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(savedStudent);
    }

    @Test
    void getAll_shouldReturnEmptyList_whenStudentDoNotExist() {
        List<StudentResponseDTO> students = studentService.getAll();

        assertThat(students)
                .isNotNull()
                .isEmpty();
    }

    @Test
    void getById_shouldReturnStudent_whenIdExists() {
        StudentRequestDTO requestDTO = createDefaultStudentRequestDTO();

        StudentResponseDTO savedStudent = studentService.create(requestDTO);

        StudentResponseDTO foundStudent = studentService.getById(savedStudent.id());

        assertThat(foundStudent)
                .usingRecursiveComparison()
                .isEqualTo(savedStudent);
    }

    @Test
    void getById_shouldThrowException_whenIdDoesNotExist() {
        assertThatThrownBy(() -> studentService.getById(nonExistingId))
                .isInstanceOf(StudentNotFoundException.class);
    }

    @Test
    void updateStudent_shouldUpdateStudent_whenIdExists() {
        StudentRequestDTO requestDTO = createDefaultStudentRequestDTO();

        StudentResponseDTO savedStudent = studentService.create(requestDTO);

        StudentUpdateDTO updateDTO = new StudentUpdateDTO(Instancio.create(String.class), Instancio.create(String.class));

        StudentResponseDTO updatedStudent = studentService.update(savedStudent.id(), updateDTO);

        assertThat(updatedStudent.id()).isEqualTo(savedStudent.id());
        assertThat(updatedStudent.studentName()).isEqualTo(updateDTO.studentName());
        assertThat(updatedStudent.instrument()).isEqualTo(updateDTO.instrument());
    }

    @Test
    void updateStudent_shouldThrowException_whenIdDoesNotExist() {
        StudentUpdateDTO updateDTO = new StudentUpdateDTO(Instancio.create(String.class), Instancio.create(String.class));

        assertThatThrownBy(() -> studentService.update(nonExistingId, updateDTO))
                .isInstanceOf(StudentNotFoundException.class);
    }

    @Test
    void deleteStudent_shouldDeleteStudent_whenIdExists() {
        StudentRequestDTO requestDTO = createDefaultStudentRequestDTO();

        StudentResponseDTO savedStudent = studentService.create(requestDTO);

        studentService.delete(savedStudent.id());

        assertThatThrownBy(() -> studentService.delete(savedStudent.id()))
                .isInstanceOf(StudentNotFoundException.class);
    }

    @Test
    void deleteStudent_shouldThrowException_whenIdDoesNotExist() {
        assertThatThrownBy(() -> studentService.delete(nonExistingId))
                .isInstanceOf(StudentNotFoundException.class);
    }

}
