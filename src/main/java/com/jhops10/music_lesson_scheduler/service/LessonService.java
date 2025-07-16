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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final StudentRepository studentRepository;

    public LessonResponseDTO create(LessonRequestDTO dto) {
        Student student = studentRepository.findById(dto.studentId())
                .orElseThrow(() -> new StudentNotFoundException("Aluno com o id " + dto.studentId() + " não encontrado."));

        Lesson lesson = Lesson.builder()
                .startTime(dto.startTime())
                .student(student)
                .notifyBeforeMinutes(dto.notifyBeforeMinutes())
                .build();

        Lesson saved = lessonRepository.save(lesson);
        return LessonResponseDTO.fromEntity(saved);
    }

    public List<LessonResponseDTO> getAll() {
        return lessonRepository.findAll().stream()
                .map(LessonResponseDTO::fromEntity)
                .toList();
    }

    public LessonResponseDTO getById(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new LessonNotFoundException("Aula com id " + id + " não encontrada."));
        return LessonResponseDTO.fromEntity(lesson);
    }

    public LessonResponseDTO update(Long id, LessonUpdateDTO updateDTO) {
        Lesson existing = lessonRepository.findById(id)
                .orElseThrow(() -> new LessonNotFoundException("Aula com id " + id + " não encontrada."));

        updateDTO.applyUpdatesTo(existing);
        Lesson updated = lessonRepository.save(existing);
        return LessonResponseDTO.fromEntity(updated);
    }

}
