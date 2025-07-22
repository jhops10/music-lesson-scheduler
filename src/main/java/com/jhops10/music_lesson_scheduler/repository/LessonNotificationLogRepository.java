package com.jhops10.music_lesson_scheduler.repository;

import com.jhops10.music_lesson_scheduler.model.LessonNotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonNotificationLogRepository extends JpaRepository<LessonNotificationLog, Long> {
}
