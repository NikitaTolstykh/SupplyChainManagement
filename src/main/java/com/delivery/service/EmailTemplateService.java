package com.delivery.service;

import com.delivery.entity.User;

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


}
