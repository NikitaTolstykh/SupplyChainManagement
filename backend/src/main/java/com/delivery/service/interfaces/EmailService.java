package com.delivery.service.interfaces;

public interface EmailService {
    void sendHtmlEmail(String to, String subject, String htmlBody);
}
