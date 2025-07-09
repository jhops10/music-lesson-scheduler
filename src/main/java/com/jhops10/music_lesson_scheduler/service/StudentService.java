package com.jhops10.music_lesson_scheduler.service;

import com.jhops10.music_lesson_scheduler.dto.student.StudentRequestDTO;
import com.jhops10.music_lesson_scheduler.dto.student.StudentResponseDTO;
import com.jhops10.music_lesson_scheduler.model.Student;
import com.jhops10.music_lesson_scheduler.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentResponseDTO create(StudentRequestDTO dto) {
        Student student = dto.toEntity();
        Student saved = studentRepository.save(student);
        return StudentResponseDTO.fromEntity(saved);
    }

}
