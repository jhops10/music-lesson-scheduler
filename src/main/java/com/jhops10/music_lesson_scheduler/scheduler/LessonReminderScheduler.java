package com.jhops10.music_lesson_scheduler.scheduler;

import com.jhops10.music_lesson_scheduler.model.Lesson;
import com.jhops10.music_lesson_scheduler.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LessonReminderScheduler {

    private final LessonRepository lessonRepository;

    @Scheduled(fixedRate = 60000)
    public void checkUpcomingLessons() {
        LocalDateTime now = LocalDateTime.now();

        List<Lesson> lessons = lessonRepository.findAll();

        for (Lesson lesson : lessons) {
            LocalDateTime notifyTime = lesson.getStartTime().minusMinutes(lesson.getNotifyBeforeMinutes());

            if (now.isAfter(notifyTime) && now.isBefore(lesson.getStartTime())) {
                System.out.printf("🔔 Aula de %s (%s) às %s [em %d minutos]%n",
                        lesson.getStudent().getStudentName(),
                        lesson.getStudent().getInstrument(),
                        lesson.getStartTime(),
                        lesson.getNotifyBeforeMinutes());
            }
        }
    }
}
