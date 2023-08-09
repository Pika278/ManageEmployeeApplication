package com.example.demo.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {EmailServiceImpl.class})
@ExtendWith(SpringExtension.class)
class EmailServiceImplTest {
    @Autowired
    private EmailServiceImpl emailServiceImpl;

    @MockBean
    private JavaMailSender javaMailSender;

    /**
     * Method under test: {@link EmailServiceImpl#sendMail(String, String, String)}
     */
    @Test
    void testSendMail() throws MailException {
        doNothing().when(javaMailSender).send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        emailServiceImpl.sendMail("alice.liddell@example.org", "Hello from the Dreaming Spires",
                "Not all who wander are lost");
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(Mockito.<MimeMessage>any());
    }

    /**
     * Method under test: {@link EmailServiceImpl#sendMail(String, String, String)}
     */
    @Test
    void testSendMail2() throws MailException {
        doThrow(new RuntimeException("foo")).when(javaMailSender).send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        assertThrows(RuntimeException.class, () -> emailServiceImpl.sendMail("alice.liddell@example.org",
                "Hello from the Dreaming Spires", "Not all who wander are lost"));
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(Mockito.<MimeMessage>any());
    }

    /**
     * Method under test: {@link EmailServiceImpl#sendMail(String, String[], String, String)}
     */
    @Test
    void testSendMail3() throws MailException {
        doNothing().when(javaMailSender).send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        emailServiceImpl.sendMail("alice.liddell@example.org", new String[]{"ada.lovelace@example.org"},
                "Hello from the Dreaming Spires", "Not all who wander are lost");
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(Mockito.<MimeMessage>any());
    }

    /**
     * Method under test: {@link EmailServiceImpl#sendMail(String, String[], String, String)}
     */
    @Test
    void testSendMail4() throws MailException {
        doThrow(new RuntimeException("foo")).when(javaMailSender).send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        assertThrows(RuntimeException.class, () -> emailServiceImpl.sendMail("alice.liddell@example.org",
                new String[]{"ada.lovelace@example.org"}, "Hello from the Dreaming Spires", "Not all who wander are lost"));
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(Mockito.<MimeMessage>any());
    }
}

