package com.delivery.event;

import com.delivery.service.EmailService;
import com.delivery.service.EmailTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailNotificationListener {
    private final EmailService emailService;
    private final EmailTemplateService emailTemplateService;

    public EmailNotificationListener(EmailService emailService, EmailTemplateService emailTemplateService) {
        this.emailService = emailService;
        this.emailTemplateService = emailTemplateService;
    }

    @EventListener
    @Async
    public void handleWorkerCreated(WorkerCreatedEvent event) {
        log.info("Sending worker created email to: {}", event.getWorker().getEmail());

        String subject = "Created account at delivery system";
        String body = emailTemplateService.createWorkerAccountEmail(event.getWorker(), event.getPassword());

        emailService.sendHtmlEmail(event.getWorker().getEmail(), subject, body);
    }
}
