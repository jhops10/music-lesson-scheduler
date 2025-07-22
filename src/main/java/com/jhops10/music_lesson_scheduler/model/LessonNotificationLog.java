package com.jhops10.music_lesson_scheduler.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_notificaton_logs")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonNotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @Column(nullable = false)
    private LocalDateTime notifiedAt;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private String deliveryMethod;

}
