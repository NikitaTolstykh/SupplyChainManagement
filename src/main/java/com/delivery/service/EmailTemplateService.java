package com.delivery.service;

import com.delivery.entity.User;
import com.delivery.entity.Vehicle;

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

}
