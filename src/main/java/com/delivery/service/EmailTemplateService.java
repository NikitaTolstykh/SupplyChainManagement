package com.delivery.service;

import com.delivery.entity.Order;
import com.delivery.entity.User;
import com.delivery.entity.Vehicle;
import com.delivery.util.OrderStatus;

public class EmailTemplateService {

    public String getWorkerAccountEmail(User worker, String password) {
        return String.format("""
                <html>
                <body>
                    <h2>Welcome to our corporative app!</h2>
                    <p>Hello, %s!</p>
                    <p>An account has been created for you in the delivery system.</p>
                    <p><strong>Login details:</strong></p>
                    <ul>
                        <li>Email: %s</li>
                        <li>Password: %s</li>
                    </ul>
                    <p>Sincerely,<br>Delivery system team</p>
                </body>
                </html>
                """, worker.getFirstName(), worker.getEmail(), password);
    }

    public String createVehicleAssignmentEmail(User driver, Vehicle vehicle) {
        return String.format("""
                        <html>
                        <body>
                            <h2>Vehicle assignment</h2>
                            <p>Hello, %s!</p>
                            <p>You have been assigned a vehicle:</p>
                            <ul>
                                <li>Brand: %s</li>
                                <li>Model: %s</li>
                                <li>Color: %s</li>
                                <li>License plate: %s</li>
                            </ul>
                            <p>Treat your vehicle responsibly!</p>
                            <p> Sincerely,<br>Delivery system team</p>
                        </body>
                        </html>
                        """, driver.getFirstName(), vehicle.getBrand(), vehicle.getModel(),
                vehicle.getLicensePlate(), vehicle.getColor());
    }

    public String createOrderCreatedEmail(Order order) {
        return String.format("""
                        <html>
                        <body>
                            <h2>Order successfully created!!</h2>
                            <p>Hello, %s!</p>
                            <p>Your order #%d has been successfully created.</p>
                            <p><strong>Order details::</strong></p>
                            <ul>
                                <li>From: %s</li>
                                <li>To: %s</li>
                                <li>Cargo type: %s</li>
                                <li>Weight: %s кг</li>
                                <li>Cost: %s руб.</li>
                                <li>Pickup time: %s</li>
                            </ul>
                            <p>Order status: %s</p>
                            <p>We will notify you about the change in order status.</p>
                            <p>Best regards,<br>Delivery system team</p>
                        </body>
                        </html>
                        """, order.getClient().getFirstName(), order.getId(),
                order.getFromAddress(), order.getToAddress(), order.getCargoType(),
                order.getWeightKg(), order.getPrice(), order.getPickupTime(),
                order.getStatus());
    }


    private String getStatusMessage(OrderStatus status) {
        return switch (status) {
            case CREATED -> "Your order has been created and is awaiting processing.";
            case ASSIGNED -> "The order has been assigned to the driver.";
            case ACCEPTED -> "The driver has accepted the order";
            case IN_PROGRESS -> "The driver started processing your order";
            case DELIVERED -> "The order has been successfully delivered!";
            case CANCELLED -> "The order has been cancelled.";
        };
    }


}