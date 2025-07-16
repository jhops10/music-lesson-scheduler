package com.jhops10.music_lesson_scheduler.controller;

import com.jhops10.music_lesson_scheduler.dto.lesson.LessonRequestDTO;
import com.jhops10.music_lesson_scheduler.dto.lesson.LessonResponseDTO;
import com.jhops10.music_lesson_scheduler.service.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @PostMapping
    public ResponseEntity<LessonResponseDTO> createLesson(@RequestBody @Valid LessonRequestDTO dto) {
        LessonResponseDTO created = lessonService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<LessonResponseDTO>> getAllLessons() {
        List<LessonResponseDTO> lessons = lessonService.getAll();
        return ResponseEntity.ok().body(lessons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonResponseDTO> getLessonById(@PathVariable("id") Long id) {
        LessonResponseDTO lesson = lessonService.getById(id);
        return ResponseEntity.ok().body(lesson);
    }
}
