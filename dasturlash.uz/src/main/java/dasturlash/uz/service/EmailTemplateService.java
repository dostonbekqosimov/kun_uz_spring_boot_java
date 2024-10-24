package dasturlash.uz.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailTemplateService {

    @Value("${spring.application.name}")
    private String appName;

    public String getRegistrationEmailTemplate(Long userId, String userName, int deadlineHours, int remainingAttempts) {
        return """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <meta charset="UTF-8">
                        <style>
                              <style>
                                                           .container {
                                               max-width: 600px;
                                               margin: 0 auto;
                                               font-family: Arial, sans-serif;
                                           }
                                                           .header {
                                               background-color: #2196F3;
                                               padding: 20px;
                                               text-align: center;
                                           }
                                                           .header h1 {
                                               color: white;
                                               margin: 0;
                                           }
                                                           .content {
                                               padding: 20px;
                                               background-color: #f9f9f9;
                                           }
                                                           .button-container {
                                               text-align: center;
                                               margin: 30px 0;
                                           }
                                                           .verify-button {
                                               background-color: #2196F3;
                                               color: white;
                                               padding: 15px 30px;
                                               text-decoration: none;
                                               border-radius: 5px;
                                               font-weight: bold;
                                           }
                                                           .footer {
                                               text-align: center;
                                               padding: 20px;
                                               color: #666;
                                               font-size: 14px;
                                           }
                                                           .welcome-message {
                                               font-size: 18px;
                                               color: #333;
                                               line-height: 1.5;
                                           }
                                                           .deadline-warning {
                                               color: #e65100;
                                               font-weight: bold;
                                               margin: 20px 0;
                                               padding: 10px;
                                               border: 1px solid #e65100;
                                               border-radius: 4px;
                                               text-align: center;
                                           }
                                                           .attempts-info {
                                               color: #2196F3;
                                               margin: 10px 0;
                                               font-size: 14px;
                                               text-align: center;
                                           }
                                                           .link-info {
                                               background-color: #f5f5f5;
                                               padding: 10px;
                                               border-radius: 4px;
                                               margin: 15px 0;
                                           }
                                           code {
                                               background-color: #e8e8e8;
                                               padding: 3px 6px;
                                               border-radius: 3px;
                                               font-family: monospace;
                                           }
                                                       </style>
                        </style>
                    </head>
                    <body>
                        <div class="container">
                            <div class="header">
                                <h1>Welcome to %s!</h1>
                            </div>
                            <div class="content">
                                <p class="welcome-message">Hello %s,</p>
                                <p>Thank you for registering with us. We're excited to have you on board!</p>
                                <p>Please click the button below to complete your registration:</p>
                
                                <div class="button-container">
                                    <a href="http://localhost:8080/auth/registration/confirm/%d" 
                                       class="verify-button" target="_blank">
                                        Verify Your Email
                                    </a>
                                </div>
                
                                <div class="deadline-warning">
                                    ⚠️ Important: This verification link will expire in %d hours
                                </div>
                
                                <div class="attempts-info">
                                    You have %d remaining confirmation attempts
                                </div>
                
                                <div class="link-info">
                                    <p>If the button doesn't work, you can copy and paste this link into your browser:</p>
                                    <p><code>http://localhost:8080/auth/registration/confirm/%d</code></p>
                                </div>
                
                                <p>If the link has expired, <a href="http://localhost:8080/auth/registration/resend/%d" target="_blank">click here to request a new confirmation email</a>.</p>
                            </div>
                            <div class="footer">
                                <p>This is an automated message. Please do not reply to this email.</p>
                                <p>If you need assistance, please contact our support team.</p>
                                <p>&copy; 2024 %s. All rights reserved.</p>
                            </div>
                        </div>
                    </body>
                    </html>
                """.formatted(appName, userName, userId, deadlineHours, remainingAttempts, userId, userId, appName);
    }


    // You might want to add a method for resend confirmation emails with slightly different wording
    public String getResendConfirmationEmailTemplate(Long userId, String userName, int deadlineHours, int remainingAttempts) {
        return """
                <!DOCTYPE html>
                <html>
                <!-- Same style section as above -->
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>%s - Registration Confirmation</h1>
                        </div>
                        <div class="content">
                            <p class="welcome-message">Hello %s,</p>
                            <p>A new confirmation link has been generated for your registration.</p>
                            <p>Please click the button below to complete your registration:</p>
                
                            <div class="button-container">
                                <a href="http://localhost:8080/auth/registration/confirm/%d" 
                                   class="verify-button" target="_blank">
                                    Verify Your Email
                                </a>
                            </div>
                
                            <div class="deadline-warning">
                                ⚠️ Important: This verification link will expire in %d hours
                            </div>
                
                            <div class="attempts-info">
                                ⚠️ You have %d remaining confirmation attempts
                            </div>
                
                            <div class="link-info">
                                <p>If the button doesn't work, you can copy and paste this link into your browser:</p>
                                <p><code>http://localhost:8080/auth/registration/confirm/%d</code></p>
                            </div>
                        </div>
                        <div class="footer">
                            <p>This is an automated message. Please do not reply to this email.</p>
                            <p>If you need assistance, please contact our support team.</p>
                            <p>&copy; 2024 %s. All rights reserved.</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(appName, userName, userId, deadlineHours, remainingAttempts, userId, appName);
    }
}