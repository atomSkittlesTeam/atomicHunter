package net.vniia.skittles.services;

import jakarta.mail.MessagingException;

import java.io.IOException;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);
    void sendEmailWithAttachment(String toAddress, String subject, String message, String attachment)
            throws MessagingException, IOException;
    void sendCalendarInvite(String subject, String consumerEmail, Long vacancyId) throws Exception;
}
