package com.auca.event_registration_system.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    
    private final JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    /**
     * Sends a simple text email
     */
    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
    
    /**
     * Sends an HTML email with registration confirmation
     */
    public void sendRegistrationConfirmation(String to, String userName, String eventName, 
                                            String eventDate, String venue, String ticketNumber, 
                                            String qrCodeBase64) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Event Registration Confirmation - " + eventName);
            
            String htmlContent = buildRegistrationEmailHtml(userName, eventName, eventDate, 
                                                          venue, ticketNumber, qrCodeBase64);
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error sending email", e);
        }
    }
    
    private String buildRegistrationEmailHtml(String userName, String eventName, String eventDate,
                                            String venue, String ticketNumber, String qrCodeBase64) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; }" +
                ".content { padding: 20px; background-color: #f9f9f9; }" +
                ".ticket-info { background-color: white; padding: 15px; margin: 15px 0; border-left: 4px solid #4CAF50; }" +
                ".qr-code { text-align: center; margin: 20px 0; }" +
                ".footer { text-align: center; padding: 20px; color: #666; font-size: 12px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h1>Event Registration Confirmed!</h1>" +
                "</div>" +
                "<div class='content'>" +
                "<p>Dear " + userName + ",</p>" +
                "<p>Your registration for the following event has been confirmed:</p>" +
                "<div class='ticket-info'>" +
                "<h3>" + eventName + "</h3>" +
                "<p><strong>Date:</strong> " + eventDate + "</p>" +
                "<p><strong>Venue:</strong> " + venue + "</p>" +
                "<p><strong>Ticket Number:</strong> " + ticketNumber + "</p>" +
                "</div>" +
                "<div class='qr-code'>" +
                "<p><strong>Your QR Code Ticket:</strong></p>" +
                "<img src='" + qrCodeBase64 + "' alt='QR Code' style='max-width: 300px;' />" +
                "</div>" +
                "<p>Please present this QR code at the event entrance.</p>" +
                "<p>We look forward to seeing you there!</p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>This is an automated message. Please do not reply.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}

