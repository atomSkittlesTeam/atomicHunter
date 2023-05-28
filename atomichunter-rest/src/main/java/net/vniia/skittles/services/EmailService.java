package net.vniia.skittles.services;

import jakarta.mail.MessagingException;

import java.io.FileNotFoundException;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);
    void sendEmailWithAttachment(String toAddress, String subject, String message, String attachment)
            throws MessagingException, FileNotFoundException;
}
