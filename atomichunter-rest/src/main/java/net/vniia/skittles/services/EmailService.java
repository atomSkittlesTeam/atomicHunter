package net.vniia.skittles.services;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.property.Method;
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
import net.vniia.skittles.entities.ConfirmationToken;
import net.vniia.skittles.repositories.ConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Log4j2
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;

    private final ConfirmationTokenRepository confirmationTokenRepository;

    private final ResourceHelper resourceHelper;

    @Value("${server.port}")
    int runningPort;

    @Value("classpath:/invite_mail.html")
    private Resource inviteRespondMailHtml;

    @Value("classpath:/invite_mail2.html")
    private Resource inviteEmployeeMailHtml;

    @Value("${spring.mail.username}")
    private String serviceMail;

    @Value("${server.address:#{null}}")
    private String serviceAddress;

    public void sendSimpleMessage(
            String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(serviceMail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            try {
                emailSender.send(message);
                log.info("Invite for interview sent");
            } catch (Exception e) {
                log.error("Cant send email");
                log.error(e);
            }

        } catch (Exception e) {
            return;
        }
    }

    public void sendEmailWithAttachment(String toAddress, String subject, String message, String attachment)
            throws MessagingException, IOException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);

        InternetAddress sender = new InternetAddress(serviceMail, "Atomic Hunter");
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

        try {
            emailSender.send(mimeMessage);
            log.info("Invite for interview sent");
        } catch (Exception e) {
            log.error("Cant send email");
            log.error(e);
        }
    }

    public static String generateICalDataForInterviewInvite(
            String summary,
            String description,
            Date interviewStartDate,
            Date interviewEndDate) throws IOException {
        ICalendar ical = new ICalendar();
        ical.addProperty(new Method(Method.PUBLISH));

        VEvent event = new VEvent();
//        event.setSummary("Приглашение на собеседование");
//        event.setDescription("Собеседование в компании Атомпродукт");
        event.setSummary(summary);
        event.setDescription(description);
        event.setDateStart(interviewStartDate);
        event.setDateEnd(interviewEndDate);
//        event.setDuration(new Duration.Builder()
//                .hours(1)
//                .build());

//        event.setOrganizer(new Organizer("Atomic Hunter", MailConfiguration.SERVICE_EMAIL));

//        Attendee a = new Attendee(consumerEmail, consumerEmail);
//        a.setParticipationLevel(ParticipationLevel.REQUIRED);
//        event.addAttendee(a);
        // сюда можно докинуть всех участников собрания
//        event.addAttendee(new Attendee("artemsrv3@gmail.com", "artemsrv3@gmail.com"));
        ical.addEvent(event);
        return Biweekly.write(ical).go();
    }

    public void sendInterviewInviteForRespond(String subject,
                                              List<String> consumerEmail,
                                              String summary,
                                              String description,
                                              Date interviewStartDate,
                                              Date interviewEndDate,
                                              String vacancyPosition) throws Exception {

        MimeMessage mimeMessage = emailSender.createMimeMessage();
        InternetAddress sender = new InternetAddress(serviceMail, "Atomic Hunter");

        mimeMessage.setFrom(sender);

        for (String s : consumerEmail) {
            InternetAddress consumer = new InternetAddress(s);
            mimeMessage.addRecipient(Message.RecipientType.TO, consumer);
        }

        mimeMessage.setSubject(subject);

        StringBuilder builder = new StringBuilder();

        builder.append(generateICalDataForInterviewInvite(summary, description, interviewStartDate, interviewEndDate));

        MimeBodyPart messageBodyPart = new MimeBodyPart();

        messageBodyPart.setHeader("Content-Class", "urn:content-classes:calendarmessage");
        messageBodyPart.setHeader("Content-ID", "calendar_message");
        messageBodyPart.setDataHandler(new DataHandler(
                new ByteArrayDataSource(builder.toString(), "text/calendar;method=PUBLISH;name=\"invite.ics\"")));

        MimeBodyPart textPart = new MimeBodyPart();

        String html = resourceHelper.getResourceAsString(this.inviteRespondMailHtml);

        html = html.replaceAll("на позицию aaaa", String.format("на позицию \"%s\"", vacancyPosition));
        textPart.setContent(html, "text/html; charset=utf-8");

        MimeMultipart multipart = new MimeMultipart();

        multipart.addBodyPart(textPart);
        multipart.addBodyPart(messageBodyPart);

        mimeMessage.setContent(multipart);

        try {
            emailSender.send(mimeMessage);
            log.info("Invite for interview sent");
        } catch (Exception e) {
            log.error("Cant send email");
            log.error(e);
        }
    }

    private ConfirmationToken createConfirmationTokenInterviewInvite(String email, Long vacancyRespondId) {
        ConfirmationToken confirmationToken = new ConfirmationToken(email, vacancyRespondId);
        confirmationToken = confirmationTokenRepository.save(confirmationToken);
        return confirmationToken;
    }

    public void sendInterviewInviteForEmployee(String subject,
                                               List<String> consumerEmail,
                                               String summary,
                                               String description,
                                               Date interviewStartDate,
                                               Date interviewEndDate,
                                               String vacancyPosition) throws Exception {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        InternetAddress sender = new InternetAddress(serviceMail, "Atomic Hunter");

        mimeMessage.setFrom(sender);

        for (String s : consumerEmail) {
            InternetAddress consumer = new InternetAddress(s);
            mimeMessage.addRecipient(Message.RecipientType.TO, consumer);
        }

        mimeMessage.setSubject(subject);

        StringBuilder builder = new StringBuilder();

        builder.append(generateICalDataForInterviewInvite(summary, description, interviewStartDate, interviewEndDate));

        MimeBodyPart messageBodyPart = new MimeBodyPart();

        messageBodyPart.setHeader("Content-Class", "urn:content-classes:calendarmessage");
        messageBodyPart.setHeader("Content-ID", "calendar_message");
        messageBodyPart.setDataHandler(new DataHandler(
                new ByteArrayDataSource(builder.toString(), "text/calendar;method=PUBLISH;name=\"invite.ics\"")));

        MimeBodyPart textPart = new MimeBodyPart();

        String html = resourceHelper.getResourceAsString(this.inviteEmployeeMailHtml);

        html = html.replaceAll("на позицию aaaa", String.format("на позицию \"%s\"", vacancyPosition));
        textPart.setContent(html, "text/html; charset=utf-8");

        MimeMultipart multipart = new MimeMultipart();

        multipart.addBodyPart(textPart);
        multipart.addBodyPart(messageBodyPart);

        mimeMessage.setContent(multipart);

        try {
            emailSender.send(mimeMessage);
            log.info("Invite for interview sent");
        } catch (Exception e) {
            log.error("Cant send email");
            log.error(e);
        }
    }
}