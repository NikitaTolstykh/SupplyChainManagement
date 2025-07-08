package com.delivery.service;

import com.delivery.entity.Order;
import com.delivery.entity.User;
import com.delivery.entity.Vehicle;
import com.delivery.util.OrderStatus;
import org.springframework.stereotype.Service;

@Service
public class EmailTemplateService {

    private static final String BASE_STYLES = """
            <style>
                body {
                    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                    line-height: 1.6;
                    margin: 0;
                    padding: 20px;
                    background-color: #f5f5f5;
                    color: #333;
                }
                .container {
                    max-width: 600px;
                    margin: 0 auto;
                    background-color: white;
                    border-radius: 12px;
                    box-shadow: 0 4px 20px rgba(0,0,0,0.1);
                    overflow: hidden;
                }
                .header {
                    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                    color: white;
                    padding: 30px;
                    text-align: center;
                }
                .header h1 {
                    margin: 0;
                    font-size: 28px;
                    font-weight: 300;
                }
                .header p {
                    margin: 10px 0 0 0;
                    font-size: 16px;
                    opacity: 0.9;
                }
                .content {
                    padding: 30px;
                }
                .info-section {
                    background-color: #f8f9fa;
                    border-radius: 8px;
                    padding: 20px;
                    margin: 20px 0;
                    border-left: 4px solid #667eea;
                }
                .info-row {
                    display: flex;
                    justify-content: space-between;
                    align-items: center;
                    padding: 12px 0;
                    border-bottom: 1px solid #e9ecef;
                }
                .info-row:last-child {
                    border-bottom: none;
                }
                .label {
                    font-weight: 600;
                    color: #495057;
                    font-size: 14px;
                }
                .value {
                    color: #6c757d;
                    font-size: 14px;
                    font-weight: 500;
                }
                .highlight {
                    background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
                    padding: 20px;
                    border-radius: 8px;
                    margin: 20px 0;
                    text-align: center;
                }
                .status-badge {
                    display: inline-block;
                    padding: 8px 16px;
                    border-radius: 20px;
                    font-size: 12px;
                    font-weight: 600;
                    text-transform: uppercase;
                    letter-spacing: 0.5px;
                }
                .status-created { background-color: #17a2b8; color: white; }
                .status-assigned { background-color: #ffc107; color: #212529; }
                .status-accepted { background-color: #28a745; color: white; }
                .status-in-progress { background-color: #fd7e14; color: white; }
                .status-delivered { background-color: #28a745; color: white; }
                .status-cancelled { background-color: #dc3545; color: white; }
                .footer {
                    background-color: #f8f9fa;
                    padding: 20px;
                    text-align: center;
                    border-top: 1px solid #e9ecef;
                    color: #6c757d;
                    font-size: 12px;
                }
                .btn {
                    display: inline-block;
                    padding: 12px 24px;
                    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                    color: white;
                    text-decoration: none;
                    border-radius: 6px;
                    font-weight: 600;
                    margin: 10px 0;
                }
                .price {
                    font-size: 18px;
                    font-weight: 700;
                    color: #28a745;
                }
                .alert {
                    padding: 15px;
                    border-radius: 6px;
                    margin: 15px 0;
                }
                .alert-info {
                    background-color: #d1ecf1;
                    border: 1px solid #bee5eb;
                    color: #0c5460;
                }
                .alert-success {
                    background-color: #d4edda;
                    border: 1px solid #c3e6cb;
                    color: #155724;
                }
                .emoji {
                    font-size: 24px;
                    margin-right: 10px;
                }
            </style>
            """;

    public String createWorkerAccountEmail(User worker, String password) {
        return String.format("""
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Welcome to our corporate app</title>
                    %s
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1><span class="emoji">🎉</span>Welcome to our corporate app!</h1>
                            <p>Hello, %s!</p>
                        </div>
                
                        <div class="content">
                            <p>An account has been created for you in the delivery system.</p>
                
                            <div class="info-section">
                                <h3><span class="emoji">🔐</span>Login details</h3>
                                <div class="info-row">
                                    <span class="label">Email:</span>
                                    <span class="value">%s</span>
                                </div>
                                <div class="info-row">
                                    <span class="label">Password:</span>
                                    <span class="value">%s</span>
                                </div>
                            </div>
                
                            <div class="alert alert-info">
                                <strong>Important:</strong> Please change your password after first login for security purposes.
                            </div>
                        </div>
                
                        <div class="footer">
                            <p>Sincerely,<br>Delivery system team</p>
                            <p>This is an automated message, please do not reply.</p>
                        </div>
                    </div>
                </body>
                </html>
                """, BASE_STYLES, worker.getFirstName(), worker.getEmail(), password);
    }

    public String createVehicleAssignmentEmail(User driver, Vehicle vehicle) {
        return String.format("""
                        <!DOCTYPE html>
                        <html>
                        <head>
                            <meta charset="UTF-8">
                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                            <title>Vehicle Assignment</title>
                            %s
                        </head>
                        <body>
                            <div class="container">
                                <div class="header">
                                    <h1><span class="emoji">🚗</span>Vehicle Assignment</h1>
                                    <p>Hello, %s!</p>
                                </div>
                        
                                <div class="content">
                                    <p>You have been assigned a vehicle for order deliveries.</p>
                        
                                    <div class="info-section">
                                        <h3><span class="emoji">🚐</span>Vehicle Information</h3>
                                        <div class="info-row">
                                            <span class="label">Brand:</span>
                                            <span class="value">%s</span>
                                        </div>
                                        <div class="info-row">
                                            <span class="label">Model:</span>
                                            <span class="value">%s</span>
                                        </div>
                                        <div class="info-row">
                                            <span class="label">Color:</span>
                                            <span class="value">%s</span>
                                        </div>
                                        <div class="info-row">
                                            <span class="label">License plate:</span>
                                            <span class="value"><strong>%s</strong></span>
                                        </div>
                                    </div>
                        
                                    <div class="alert alert-info">
                                        <strong>Reminder:</strong> Treat your vehicle responsibly and follow all operating guidelines!
                                    </div>
                                </div>
                        
                                <div class="footer">
                                    <p>Sincerely,<br>Delivery system team</p>
                                    <p>This is an automated message, please do not reply.</p>
                                </div>
                            </div>
                        </body>
                        </html>
                        """, BASE_STYLES, driver.getFirstName(), vehicle.getBrand(), vehicle.getModel(),
                vehicle.getColor(), vehicle.getLicensePlate());
    }

    public String createOrderCreatedEmail(Order order) {
        return String.format("""
                        <!DOCTYPE html>
                        <html>
                        <head>
                            <meta charset="UTF-8">
                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                            <title>Order Created</title>
                            %s
                        </head>
                        <body>
                            <div class="container">
                                <div class="header">
                                    <h1><span class="emoji">🚚</span>Order successfully created!</h1>
                                    <p>Hello, %s!</p>
                                </div>
                        
                                <div class="content">
                                    <div class="highlight">
                                        <h3>Your order #%d has been successfully created!</h3>
                                        <span class="status-badge status-created">%s</span>
                                    </div>
                        
                                    <div class="info-section">
                                        <h3><span class="emoji">📋</span>Order details</h3>
                                        <div class="info-row">
                                            <span class="label">From:</span>
                                            <span class="value">%s</span>
                                        </div>
                                        <div class="info-row">
                                            <span class="label">To:</span>
                                            <span class="value">%s</span>
                                        </div>
                                        <div class="info-row">
                                            <span class="label">Cargo type:</span>
                                            <span class="value">%s</span>
                                        </div>
                                        <div class="info-row">
                                            <span class="label">Weight:</span>
                                            <span class="value">%s kg</span>
                                        </div>
                                        <div class="info-row">
                                            <span class="label">Pickup time:</span>
                                            <span class="value">%s</span>
                                        </div>
                                        <div class="info-row">
                                            <span class="label">Cost:</span>
                                            <span class="value price">%s pln</span>
                                        </div>
                                    </div>
                        
                                    <div class="alert alert-info">
                                        <strong><span class="emoji">📞</span>What's next?</strong>
                                        <ul style="margin: 10px 0 0 20px; text-align: left;">
                                            <li>We will find a suitable driver for your order</li>
                                            <li>You will receive a notification about driver assignment</li>
                                            <li>The driver will contact you to clarify details</li>
                                            <li>Track your order status in your personal account</li>
                                        </ul>
                                    </div>
                                </div>
                        
                                <div class="footer">
                                    <p>Thank you for using our delivery service!</p>
                                    <p>📧 support@delivery.com | 📞 48-735-559-253</p>
                                    <hr style="margin: 20px 0;">
                                    <p>This is an automated message, please do not reply.</p>
                                </div>
                            </div>
                        </body>
                        </html>
                        """, BASE_STYLES, order.getClient().getFirstName(), order.getId(),
                getStatusDisplayName(order.getStatus()), order.getFromAddress(), order.getToAddress(),
                order.getCargoType(), order.getWeightKg(), order.getPickupTime(), order.getPrice());
    }

    public String createOrderStatusChangeEmail(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        String statusMessage = getStatusMessage(newStatus);
        String additionalInfo = getAdditionalStatusInfo(order, newStatus);
        String statusClass = getStatusClass(newStatus);
        String oldStatusClass = getStatusClass(oldStatus);

        return String.format("""
                        <!DOCTYPE html>
                        <html>
                        <head>
                            <meta charset="UTF-8">
                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                            <title>Order Status Update</title>
                            %s
                        </head>
                        <body>
                            <div class="container">
                                <div class="header">
                                    <h1><span class="emoji">🔄</span>Order Status Update</h1>
                                    <p>Hello, %s!</p>
                                </div>
                        
                                <div class="content">
                                    <div class="highlight">
                                        <h3>The status of your order #%d has changed</h3>
                                        <div style="margin: 15px 0;">
                                            <span class="status-badge %s">%s</span>
                                            <span style="margin: 0 10px;">→</span>
                                            <span class="status-badge %s">%s</span>
                                        </div>
                                    </div>
                        
                                    <div class="info-section">
                                        <h3><span class="emoji">📢</span>Information</h3>
                                        <p>%s</p>
                                    </div>
                        
                                    %s
                                </div>
                        
                                <div class="footer">
                                    <p>Sincerely,<br>Delivery system team</p>
                                    <p>This is an automated message, please do not reply.</p>
                                </div>
                            </div>
                        </body>
                        </html>
                        """, BASE_STYLES, order.getClient().getFirstName(), order.getId(),
                oldStatusClass, getStatusDisplayName(oldStatus), statusClass, getStatusDisplayName(newStatus),
                statusMessage, additionalInfo);
    }

    public String createOrderAssignedToDriverEmail(Order order, User driver) {
        return String.format("""
                        <!DOCTYPE html>
                        <html>
                        <head>
                            <meta charset="UTF-8">
                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                            <title>New Order Assigned</title>
                            %s
                        </head>
                        <body>
                            <div class="container">
                                <div class="header">
                                    <h1><span class="emoji">📦</span>New Order Assigned</h1>
                                    <p>Hello, %s!</p>
                                </div>
                        
                                <div class="content">
                                    <div class="highlight">
                                        <h3>You have been assigned order #%d</h3>
                                        <span class="status-badge status-assigned">Assigned</span>
                                    </div>
                        
                                    <div class="info-section">
                                        <h3><span class="emoji">📋</span>Order details</h3>
                                        <div class="info-row">
                                            <span class="label">From:</span>
                                            <span class="value">%s</span>
                                        </div>
                                        <div class="info-row">
                                            <span class="label">To:</span>
                                            <span class="value">%s</span>
                                        </div>
                                        <div class="info-row">
                                            <span class="label">Cargo type:</span>
                                            <span class="value">%s</span>
                                        </div>
                                        <div class="info-row">
                                            <span class="label">Weight:</span>
                                            <span class="value">%s kg</span>
                                        </div>
                                        <div class="info-row">
                                            <span class="label">Pickup time:</span>
                                            <span class="value">%s</span>
                                        </div>
                                        <div class="info-row">
                                            <span class="label">Comment:</span>
                                            <span class="value">%s</span>
                                        </div>
                                    </div>
                        
                                    <div class="alert alert-info">
                                        <strong>Action required:</strong> Please confirm the order in the system.
                                    </div>
                                </div>
                        
                                <div class="footer">
                                    <p>Sincerely,<br>Delivery system team</p>
                                    <p>This is an automated message, please do not reply.</p>
                                </div>
                            </div>
                        </body>
                        </html>
                        """, BASE_STYLES, driver.getFirstName(), order.getId(),
                order.getFromAddress(), order.getToAddress(), order.getCargoType(),
                order.getWeightKg(), order.getPickupTime(),
                order.getComment() != null ? order.getComment() : "None");
    }

    public String createOrderRatingRequestEmail(Order order) {
        return String.format("""
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Rate Your Delivery Experience</title>
                    %s
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1><span class="emoji">⭐</span>Rate Your Delivery Experience</h1>
                            <p>Hello, %s!</p>
                        </div>
                
                        <div class="content">
                            <div class="alert alert-success">
                                <h3><span class="emoji">🎉</span>Your order #%d has been successfully delivered!</h3>
                            </div>
                
                            <div class="info-section">
                                <h3><span class="emoji">💬</span>Your feedback matters to us</h3>
                                <p>We would greatly appreciate it if you could rate the quality of our service.</p>
                                <p>Your reviews help us improve and serve you better!</p>
                            </div>
                
                            <div style="text-align: center; margin: 30px 0;">
                                <p>Please log in to your account and leave a review:</p>
                                <a href="#" class="btn">Leave a Review</a>
                            </div>
                        </div>
                
                        <div class="footer">
                            <p>Thank you for choosing our service!</p>
                            <p>Sincerely,<br>Delivery system team</p>
                            <p>This is an automated message, please do not reply.</p>
                        </div>
                    </div>
                </body>
                </html>
                """, BASE_STYLES, order.getClient().getFirstName(), order.getId());
    }

    private String getStatusMessage(OrderStatus status) {
        return switch (status) {
            case CREATED -> "Your order has been created and is awaiting processing.";
            case ASSIGNED -> "The order has been assigned to a driver.";
            case ACCEPTED -> "The driver has accepted the order.";
            case IN_PROGRESS -> "The driver started processing your order.";
            case DELIVERED -> "The order has been successfully delivered!";
            case CANCELLED -> "The order has been cancelled.";
        };
    }

    private String getAdditionalStatusInfo(Order order, OrderStatus status) {
        if (status == OrderStatus.ASSIGNED && order.getDriver() != null) {
            return String.format("""
                            <div class="info-section">
                                <h3><span class="emoji">👨‍💼</span>Driver Information</h3>
                                <div class="info-row">
                                    <span class="label">Driver:</span>
                                    <span class="value">%s %s</span>
                                </div>
                                <div class="info-row">
                                    <span class="label">Phone number:</span>
                                    <span class="value">%s</span>
                                </div>
                            </div>
                            """, order.getDriver().getFirstName(), order.getDriver().getLastName(),
                    order.getDriver().getPhone());
        }
        if (status == OrderStatus.DELIVERED) {
            return """
                    <div class="alert alert-info">
                        <strong>Don't forget:</strong> Please rate the quality of delivery in your personal account.
                    </div>
                    """;
        }
        return "";
    }

    private String getStatusDisplayName(OrderStatus status) {
        return switch (status) {
            case CREATED -> "Created";
            case ASSIGNED -> "Assigned";
            case ACCEPTED -> "Accepted";
            case IN_PROGRESS -> "In Progress";
            case DELIVERED -> "Delivered";
            case CANCELLED -> "Cancelled";
        };
    }

    private String getStatusClass(OrderStatus status) {
        return switch (status) {
            case CREATED -> "status-created";
            case ASSIGNED -> "status-assigned";
            case ACCEPTED -> "status-accepted";
            case IN_PROGRESS -> "status-in-progress";
            case DELIVERED -> "status-delivered";
            case CANCELLED -> "status-cancelled";
        };
    }
}