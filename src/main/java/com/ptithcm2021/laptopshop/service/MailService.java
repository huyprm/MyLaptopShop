package com.ptithcm2021.laptopshop.service;

import jakarta.mail.MessagingException;

public interface MailService {
    void sendMimeEmail(String email, String subject, String content) throws MessagingException;
}
