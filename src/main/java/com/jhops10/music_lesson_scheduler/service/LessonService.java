package com.jhops10.music_lesson_scheduler.service;

import com.jhops10.music_lesson_scheduler.dto.lesson.LessonRequestDTO;
import com.jhops10.music_lesson_scheduler.dto.lesson.LessonResponseDTO;
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


}
