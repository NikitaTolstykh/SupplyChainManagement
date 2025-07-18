package com.delivery.event;

import com.delivery.entity.Order;
import com.delivery.service.interfaces.EmailService;
import com.delivery.service.impl.EmailTemplateService;
import com.delivery.util.OrderStatus;
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

    @EventListener
    @Async
    public void handleVehicleAssigned(VehicleAssignedEvent event) {
        log.info("Sending vehicle assigned email to: {}", event.getDriver().getEmail());

        String subject = "Vehicle assigning";
        String body = emailTemplateService.createVehicleAssignmentEmail(event.getDriver(), event.getVehicle());

        emailService.sendHtmlEmail(event.getDriver().getEmail(), subject, body);
    }

    @EventListener
    @Async
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("Sending order created email to: {}", event.getOrder().getClient().getEmail());

        String subject = "Order has been successfully created";
        String body = emailTemplateService.createOrderCreatedEmail(event.getOrder());

        emailService.sendHtmlEmail(event.getOrder().getClient().getEmail(), subject, body);
    }

    @EventListener
    @Async
    public void handleOrderStatusChanged(OrderStatusChangedEvent event) {
        log.info("Sending order status changed email to: {}", event.getOrder().getClient().getEmail());

        String subject = "Order`s status has been changed #" + event.getOrder().getId();
        String body = emailTemplateService.createOrderStatusChangeEmail(
                event.getOrder(), event.getOldStatus(), event.getNewStatus());

        emailService.sendHtmlEmail(event.getOrder().getClient().getEmail(), subject, body);

        if (event.getNewStatus() == OrderStatus.DELIVERED) {
            sendRatingRequestEmail(event.getOrder());
        }
    }

    @EventListener
    @Async
    public void handleOrderAssignedToDriver(OrderAssignedToDriverEvent event) {
        log.info("Sending order assigned email to driver: {}", event.getDriver().getEmail());

        String subject = "A new Order assigned";
        String body = emailTemplateService.createOrderAssignedToDriverEmail(event.getOrder(), event.getDriver());

        emailService.sendHtmlEmail(event.getDriver().getEmail(), subject, body);
    }

    private void sendRatingRequestEmail(Order order) {
        String subject = "Rate the delivery quantity";
        String body = emailTemplateService.createOrderRatingRequestEmail(order);

        emailService.sendHtmlEmail(order.getClient().getEmail(), subject, body);
    }

}


