package com.happyghumakkads.service;

import com.happyghumakkads.entity.Booking;
import com.happyghumakkads.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;

    public void sendContactMessage(String name, String email, String phone, String travelType, String body) {
        String to = env.getProperty("brand.email", "happyghumakkads@gmail.com");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("New Website Enquiry from " + name);
        
        StringBuilder emailBody = new StringBuilder();
        emailBody.append("New contact form submission:\n\n");
        emailBody.append("Name: ").append(name).append("\n");
        emailBody.append("Email: ").append(email).append("\n");
        emailBody.append("Phone: ").append(phone != null ? phone : "Not provided").append("\n");
        emailBody.append("Travel Type: ").append(travelType != null ? travelType : "Not specified").append("\n\n");
        emailBody.append("Message:\n").append(body).append("\n\n");
        emailBody.append("---\n");
        emailBody.append("This email was sent from the HappyGhumakkads website contact form.");
        
        message.setText(emailBody.toString());
        String from = env.getProperty("spring.mail.username", "happyghumakkads@gmail.com");
        message.setFrom(from);
        message.setReplyTo(email); // Allow direct reply to customer
        mailSender.send(message);
    }

    public void sendWelcomeEmail(User user) {
        String from = env.getProperty("spring.mail.username", "noreply@happyghumakkads.com");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Welcome to HappyGhumakkads");
        message.setText("Hello " + user.getFullName() + ",\n\n" +
                "Welcome to HappyGhumakkads! We're excited to help plan your next trip.\n\n" +
                "Best regards,\nHappyGhumakkads Team");
        message.setFrom(from);
        mailSender.send(message);
    }

    public void sendBookingConfirmationEmail(Booking booking) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(booking.getUser().getEmail());
        message.setSubject("Booking Confirmation - " + booking.getBookingReference());
        message.setText("Dear " + booking.getUser().getFullName() + ",\n\n" +
                "Thank you for your booking. Here are your details:\n" +
                "Reference: " + booking.getBookingReference() + "\n" +
                "Package: " + booking.getPackageEntity().getName() + "\n" +
                "Travel Date: " + booking.getTravelDate() + "\n" +
                "Travelers: " + booking.getNumberOfTravelers() + "\n" +
                "Amount: Rs. " + booking.getTotalAmount() + "\n\n" +
                "Please proceed to payment to confirm your booking.\n\n" +
                "Best,\nHappyGhumakkads Team");
        mailSender.send(message);
    }

    public void sendPaymentConfirmationEmail(Booking booking) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(booking.getUser().getEmail());
        message.setSubject("Payment Received - " + booking.getBookingReference());
        message.setText("Dear " + booking.getUser().getFullName() + ",\n\n" +
                "We have received your payment for booking " + booking.getBookingReference() + ".\n" +
                "Your booking is now confirmed.\n\n" +
                "Thank you for choosing HappyGhumakkads!\n");
        mailSender.send(message);
    }

    public void sendBookingCancellationEmail(Booking booking) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(booking.getUser().getEmail());
        message.setSubject("Booking Cancelled - " + booking.getBookingReference());
        message.setText("Dear " + booking.getUser().getFullName() + ",\n\n" +
                "Your booking " + booking.getBookingReference() + " has been cancelled.\n\n" +
                "If this was a mistake, please contact support.\n");
        mailSender.send(message);
    }

    public void sendAdminBookingNotification(Booking booking) {
        String to = env.getProperty("brand.email", "happyghumakkads@gmail.com");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("New Booking Received - " + booking.getBookingReference());

        StringBuilder emailBody = new StringBuilder();
        emailBody.append(" New Booking Alert!\n\n");
        emailBody.append("Booking Details:\n");
        emailBody.append("Reference: ").append(booking.getBookingReference()).append("\n");
        emailBody.append("Customer: ").append(booking.getUser().getFullName()).append("\n");
        emailBody.append("Email: ").append(booking.getUser().getEmail()).append("\n");
        emailBody.append("Phone: ").append(booking.getUser().getPhoneNumber()).append("\n");
        emailBody.append("Package: ").append(booking.getPackageEntity().getName()).append("\n");
        emailBody.append("Travel Date: ").append(booking.getTravelDate()).append("\n");
        emailBody.append("Travelers: ").append(booking.getNumberOfTravelers()).append("\n");
        emailBody.append("Total Amount: ").append(booking.getTotalAmount()).append("\n\n");

        if (booking.getSpecialRequests() != null && !booking.getSpecialRequests().isEmpty()) {
            emailBody.append("Special Requests:\n").append(booking.getSpecialRequests()).append("\n\n");
        }

        emailBody.append("---\n");
        emailBody.append("Please contact the customer to discuss the booking details and payment.\n");
        emailBody.append("Customer email: ").append(booking.getUser().getEmail()).append("\n");
        emailBody.append("Customer phone: ").append(booking.getUser().getPhoneNumber());

        message.setText(emailBody.toString());
        String from = env.getProperty("spring.mail.username", "happyghumakkads@gmail.com");
        message.setFrom(from);
        mailSender.send(message);
    }
}
