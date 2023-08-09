package com.example.demo.service;

import org.springframework.web.multipart.MultipartFile;

public interface EmailService  {
    void sendMail(String to, String[] cc, String subject, String body);
    void sendMail(String to, String subject, String body);
}