package com.jhops10.music_lesson_scheduler.service;

import com.jhops10.music_lesson_scheduler.dto.student.StudentRequestDTO;
import com.jhops10.music_lesson_scheduler.dto.student.StudentResponseDTO;
import com.jhops10.music_lesson_scheduler.dto.student.StudentUpdateDTO;
import com.jhops10.music_lesson_scheduler.exceptions.StudentNotFoundException;
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

    public List<StudentResponseDTO> getAll() {
        return studentRepository.findAll().stream()
                .map(StudentResponseDTO::fromEntity)
                .toList();
    }

    public StudentResponseDTO getById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Aluno com o id " + id + " não encontrado."));
        return StudentResponseDTO.fromEntity(student);
    }

    public StudentResponseDTO update(Long id, StudentUpdateDTO updateDTO) {
        Student existing = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Aluno com o id " + id + " não encontrado."));

        updateDTO.applyUpdatesTo(existing);

        Student updated = studentRepository.save(existing);
        return StudentResponseDTO.fromEntity(updated);
    }

}
