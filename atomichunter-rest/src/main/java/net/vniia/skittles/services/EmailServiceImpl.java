package net.vniia.skittles.services;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.parameter.ParticipationLevel;
import biweekly.property.Attendee;
import biweekly.property.Method;
import biweekly.property.Organizer;
import biweekly.util.Duration;
import jakarta.activation.DataHandler;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.vniia.skittles.configs.MailConfiguration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

@Service
@Log4j2
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender emailSender;

    @Override
    public void sendSimpleMessage(
            String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(MailConfiguration.SERVICE_EMAIL);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            emailSender.send(message);

        } catch (Exception e) {
            return;
        }
    }

    @Override
    public void sendEmailWithAttachment(String toAddress, String subject, String message, String attachment)
            throws MessagingException, IOException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);

        InternetAddress sender = new InternetAddress(MailConfiguration.SERVICE_EMAIL, "Atomic Hunter");
        InternetAddress consumer = new InternetAddress(toAddress);

        mimeMessage.setRecipient(Message.RecipientType.TO, consumer);
        mimeMessage.setSender(sender);

        messageHelper.setFrom(sender);
        messageHelper.setTo(consumer);
        messageHelper.setSubject(subject);
        messageHelper.setText(message);

        FileSystemResource file = new FileSystemResource(ResourceUtils.getFile(attachment));
        if (file.exists()) {
            messageHelper.addAttachment(
                    Objects.requireNonNull(file.getFilename()), file);
        } else {
            log.error("attachment not found!");
        }

        emailSender.send(mimeMessage);
    }

    public static String generateICalDataForInvite(String consumerEmail) throws IOException {
        ICalendar ical = new ICalendar();
        ical.addProperty(new Method(Method.REQUEST));

        VEvent event = new VEvent();
        event.setSummary("Приглашение на собеседование");
        event.setDescription("Приглашаем вас пройти собеседование в компании");

        event.setDateStart(new Date());
        event.setDuration(new Duration.Builder()
                .hours(1)
                .build());

        event.setOrganizer(new Organizer("Atomic Hunter", MailConfiguration.SERVICE_EMAIL));

        Attendee a = new Attendee(consumerEmail, consumerEmail);
        a.setParticipationLevel(ParticipationLevel.REQUIRED);
        event.addAttendee(a);
        ical.addEvent(event);
        return Biweekly.write(ical).go();
    }

    @Override
    public void sendCalendarInvite(String subject, String consumerEmail) throws Exception {

        MimeMessage mimeMessage = emailSender.createMimeMessage();
        InternetAddress sender = new InternetAddress(MailConfiguration.SERVICE_EMAIL, "Atomic Hunter");
        InternetAddress consumer = new InternetAddress(consumerEmail);

        mimeMessage.addHeaderLine("method=REQUEST");
        mimeMessage.addHeaderLine("charset=UTF-8");
        mimeMessage.addHeaderLine("component=VEVENT");
        mimeMessage.setFrom(sender);
        mimeMessage.addRecipient(Message.RecipientType.TO, consumer);
        mimeMessage.setSubject(subject);
        StringBuilder builder = new StringBuilder();
        builder.append(generateICalDataForInvite(consumerEmail));

        MimeBodyPart messageBodyPart = new MimeBodyPart();

        messageBodyPart.setHeader("Content-Class", "urn:content-classes:calendarmessage");
        messageBodyPart.setHeader("Content-ID", "calendar_message");
        messageBodyPart.setDataHandler(new DataHandler(
                new ByteArrayDataSource(builder.toString(), "text/calendar;method=REQUEST;name=\"invite.ics\"")));

        MimeMultipart multipart = new MimeMultipart();

        multipart.addBodyPart(messageBodyPart);

        mimeMessage.setContent(multipart);


        emailSender.send(mimeMessage);
        System.out.println("Calendar invite sent");
    }

}