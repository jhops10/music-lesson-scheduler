package com.jhops10.music_lesson_scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MusicLessonSchedulerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MusicLessonSchedulerApplication.class, args);
	}

}
