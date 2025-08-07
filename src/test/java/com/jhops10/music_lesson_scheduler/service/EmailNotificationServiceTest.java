package com.jhops10.music_lesson_scheduler.service;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailNotificationServiceTest {

    @InjectMocks
    private EmailNotificationService emailNotificationService;

    @Mock
    private JavaMailSender mailSender;

    private String to;
    private String subject;
    private String text;

    @BeforeEach
    void setUp() {
        to = Instancio.create(String.class);
        subject = Instancio.create(String.class);
        text = Instancio.create(String.class);
    }

    @Test
    void sendNotificationMessage_shouldSendEmailWithCorrectFields() {
        emailNotificationService.sendNotificationMessage(to, subject, text);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());


        SimpleMailMessage sentMessage = captor.getValue();

        assertThat(sentMessage.getTo()).containsExactly(to);
        assertThat(sentMessage.getSubject()).isEqualTo(subject);
        assertThat(sentMessage.getText()).isEqualTo(text);

    }

}