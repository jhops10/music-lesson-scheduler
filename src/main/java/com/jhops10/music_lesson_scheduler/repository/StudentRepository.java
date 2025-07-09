package com.jhops10.music_lesson_scheduler.repository;

import com.jhops10.music_lesson_scheduler.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
}
