package com.jhops10.music_lesson_scheduler;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class MusicLessonSchedulerApplicationTests {

	@Mock
	private JavaMailSender javaMailSender;

	@Test
	void contextLoads() {
	}

}
