package com.jhops10.music_lesson_scheduler.integration;

import com.jhops10.music_lesson_scheduler.dto.student.StudentRequestDTO;
import com.jhops10.music_lesson_scheduler.dto.student.StudentResponseDTO;
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

import static org.assertj.core.api.Assertions.assertThat;
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


}
