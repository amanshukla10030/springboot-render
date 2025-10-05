package com.happyghumakkads.service;
import com.happyghumakkads.entity.Booking;
import com.happyghumakkads.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

@Service
public class EmailService {

    private final String sendGridApiKey;
    private final String fromEmail;
    private final String fromName;
    private final Environment env;

    @Autowired
    public EmailService(
            @Value("${spring.sendgrid.api-key:${SENDGRID_API_KEY:}}") String sendGridApiKey,
            @Value("${spring.sendgrid.from-email:${SENDGRID_FROM_EMAIL:info.happyghumakkads@gmail.com}}") String fromEmail,
            @Value("${spring.sendgrid.from-name:${SENDGRID_FROM_NAME:Happy Ghumakkads}}") String fromName,
            Environment env) {
        this.sendGridApiKey = sendGridApiKey;
        this.fromEmail = fromEmail;
        this.fromName = fromName;
        this.env = env;
        
        // Validate configuration on startup
        if (this.sendGridApiKey == null || this.sendGridApiKey.isEmpty()) {
            throw new IllegalStateException("SendGrid API key is not configured. Please set spring.sendgrid.api-key in your application properties.");
        }
    }

    private void sendEmail(String to, String subject, String htmlContent) throws IOException {
        try {
            SendGrid sg = new SendGrid(sendGridApiKey);
            sg.addRequestHeader("Content-Type", "application/json");

        Email from = new Email(fromEmail, fromName);
        Email toEmail = new Email(to);
        Content content = new Content("text/html", htmlContent);
        Mail mail = new Mail(from, subject, toEmail, content);

        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

            Response response = sg.api(request);

            if (response.getStatusCode() >= 400) {
                throw new IOException("Failed to send email. Status code: " + response.getStatusCode() 
                    + ", Body: " + response.getBody());
            }
        } catch (Exception e) {
            throw new IOException("Error sending email: " + e.getMessage(), e);
        }
    }

    public void sendContactMessage(String name, String email, String phone, String travelType, String body) throws IOException {
        String to = env.getProperty("brand.email", "info.happyghumakkads@gmail.com");
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<h2>New contact form submission:</h2>");
        htmlContent.append("<p><strong>Name:</strong> ").append(name).append("</p>");
        htmlContent.append("<p><strong>Email:</strong> ").append(email).append("</p>");
        htmlContent.append("<p><strong>Phone:</strong> ").append(phone != null ? phone : "Not provided").append("</p>");
        htmlContent.append("<p><strong>Travel Type:</strong> ").append(travelType != null ? travelType : "Not specified").append("</p>");
        htmlContent.append("<p><strong>Message:</strong><br>").append(body).append("</p>");
        htmlContent.append("<hr>");
        htmlContent.append("<p><em>This email was sent from the HappyGhumakkads website contact form.</em></p>");

        sendEmail(to, "New Website Enquiry from " + name, htmlContent.toString());
    }

    public void sendWelcomeEmail(User user) throws IOException {
        String htmlContent = "<h2>Hello " + user.getFullName() + "!</h2>" +
                "<p>Welcome to HappyGhumakkads! We're excited to help plan your next trip.</p>" +
                "<p>Best regards,<br>HappyGhumakkads Team</p>";

        sendEmail(user.getEmail(), "Welcome to HappyGhumakkads", htmlContent);
    }

    public void sendBookingConfirmationEmail(Booking booking) throws IOException {
        String htmlContent = "<h2>Dear " + booking.getUser().getFullName() + "</h2>" +
                "<p>Thank you for your booking. Here are your details:</p>" +
                "<ul>" +
                "<li><strong>Reference:</strong> " + booking.getBookingReference() + "</li>" +
                "<li><strong>Package:</strong> " + booking.getPackageEntity().getName() + "</li>" +
                "<li><strong>Travel Date:</strong> " + booking.getTravelDate() + "</li>" +
                "<li><strong>Travelers:</strong> " + booking.getNumberOfTravelers() + "</li>" +
                "<li><strong>Amount:</strong> Rs. " + booking.getTotalAmount() + "</li>" +
                "</ul>" +
                "<p>Please proceed to payment to confirm your booking.</p>" +
                "<p>Best,<br>HappyGhumakkads Team</p>";

        sendEmail(booking.getUser().getEmail(), "Booking Confirmation - " + booking.getBookingReference(), htmlContent);
    }

    public void sendPaymentConfirmationEmail(Booking booking) throws IOException {
        String htmlContent = "<h2>Dear " + booking.getUser().getFullName() + "</h2>" +
                "<p>We have received your payment for booking " + booking.getBookingReference() + ".</p>" +
                "<p>Your booking is now confirmed.</p>" +
                "<p>Thank you for choosing HappyGhumakkads!</p>";

        sendEmail(booking.getUser().getEmail(), "Payment Received - " + booking.getBookingReference(), htmlContent);
    }

    public void sendBookingCancellationEmail(Booking booking) throws IOException {
        String htmlContent = "<h2>Dear " + booking.getUser().getFullName() + "</h2>" +
                "<p>Your booking " + booking.getBookingReference() + " has been cancelled.</p>" +
                "<p>If this was a mistake, please contact support.</p>";

        sendEmail(booking.getUser().getEmail(), "Booking Cancelled - " + booking.getBookingReference(), htmlContent);
    }

    public void sendAdminBookingNotification(Booking booking) throws IOException {
        String to = env.getProperty("brand.email", "info.happyghumakkads@gmail.com");
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<h2>New Booking Alert!</h2>");
        htmlContent.append("<h3>Booking Details:</h3>");
        htmlContent.append("<ul>");
        htmlContent.append("<li><strong>Reference:</strong> ").append(booking.getBookingReference()).append("</li>");
        htmlContent.append("<li><strong>Customer:</strong> ").append(booking.getUser().getFullName()).append("</li>");
        htmlContent.append("<li><strong>Email:</strong> ").append(booking.getUser().getEmail()).append("</li>");
        htmlContent.append("<li><strong>Phone:</strong> ").append(booking.getUser().getPhoneNumber()).append("</li>");
        htmlContent.append("<li><strong>Package:</strong> ").append(booking.getPackageEntity().getName()).append("</li>");
        htmlContent.append("<li><strong>Travel Date:</strong> ").append(booking.getTravelDate()).append("</li>");
        htmlContent.append("<li><strong>Travelers:</strong> ").append(booking.getNumberOfTravelers()).append("</li>");
        htmlContent.append("<li><strong>Total Amount:</strong> Rs. ").append(booking.getTotalAmount()).append("</li>");
        htmlContent.append("</ul>");

        if (booking.getSpecialRequests() != null && !booking.getSpecialRequests().isEmpty()) {
            htmlContent.append("<h3>Special Requests:</h3>");
            htmlContent.append("<p>").append(booking.getSpecialRequests()).append("</p>");
        }

        htmlContent.append("<hr>");
        htmlContent.append("<p>Please contact the customer to discuss the booking details and payment.</p>");
        htmlContent.append("<p><strong>Customer email:</strong> ").append(booking.getUser().getEmail()).append("</p>");
        htmlContent.append("<p><strong>Customer phone:</strong> ").append(booking.getUser().getPhoneNumber()).append("</p>");

        sendEmail(to, "New Booking Received - " + booking.getBookingReference(), htmlContent.toString());
    }
}
