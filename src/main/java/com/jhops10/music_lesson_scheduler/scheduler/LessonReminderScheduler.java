package com.jhops10.music_lesson_scheduler.scheduler;

import com.jhops10.music_lesson_scheduler.model.Lesson;
import com.jhops10.music_lesson_scheduler.model.LessonNotificationLog;
import com.jhops10.music_lesson_scheduler.repository.LessonNotificationLogRepository;
import com.jhops10.music_lesson_scheduler.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LessonReminderScheduler {

    private final LessonRepository lessonRepository;
    private final LessonNotificationLogRepository notificationLogRepository;


    @Scheduled(fixedRate = 60000)
    public void checkUpcomingLessons() {
        System.out.println("🔄 Verificando aulas pendentes para notificação...");
        LocalDateTime now = LocalDateTime.now();

        List<Lesson> lessons = lessonRepository.findByNotifiedFalse();
        List<Lesson> notifiedLessons = new ArrayList<>();


        for (Lesson lesson : lessons) {
            LocalDateTime notifyTime = lesson.getStartTime().minusMinutes(lesson.getNotifyBeforeMinutes());

            if (now.isAfter(notifyTime) && now.isBefore(lesson.getStartTime())) {
                System.out.printf("🔔 Aula de %s (%s) às %s [em %d minutos]%n",
                        lesson.getStudent().getStudentName(),
                        lesson.getStudent().getInstrument(),
                        lesson.getStartTime(),
                        lesson.getNotifyBeforeMinutes());
                lesson.setNotified(true);
                notifiedLessons.add(lesson);
                createNotificationLog(lesson, now);
            }
        }

        if (!notifiedLessons.isEmpty()) {
            lessonRepository.saveAll(notifiedLessons);
        }
    }

    public void createNotificationLog(Lesson lesson, LocalDateTime now) {
        LessonNotificationLog notificationLog = LessonNotificationLog.builder()
                .lesson(lesson)
                .notifiedAt(now)
                .message("O aluno " + lesson.getStudent().getStudentName() + " foi notificado.")
                .deliveryMethod("Console Log")
                .build();

        notificationLogRepository.save(notificationLog);
    }


}
