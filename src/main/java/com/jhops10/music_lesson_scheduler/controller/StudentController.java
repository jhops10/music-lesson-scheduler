package com.jhops10.music_lesson_scheduler.controller;

import com.jhops10.music_lesson_scheduler.dto.student.StudentRequestDTO;
import com.jhops10.music_lesson_scheduler.dto.student.StudentResponseDTO;
import com.jhops10.music_lesson_scheduler.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentResponseDTO> createStudent(@RequestBody @Valid StudentRequestDTO dto) {
        StudentResponseDTO created = studentService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<StudentResponseDTO>> getAllStudents() {
        List<StudentResponseDTO> students = studentService.getAll();
        return ResponseEntity.ok().body(students);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable("id") Long id) {
        StudentResponseDTO student = studentService.getById(id);
        return ResponseEntity.ok().body(student);
    }
}
