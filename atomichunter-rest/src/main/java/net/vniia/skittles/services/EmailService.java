package net.vniia.skittles.services;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.property.Method;
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
import java.net.InetAddress;
import java.util.Date;
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
    private Resource inviteMailHtml;

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

            emailSender.send(message);

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

        emailSender.send(mimeMessage);
    }

    public static String generateICalDataForInvite(String consumerEmail) throws IOException {
        ICalendar ical = new ICalendar();
        ical.addProperty(new Method(Method.PUBLISH));

        VEvent event = new VEvent();
        event.setSummary("Приглашение на собеседование");
        event.setDescription("Приглашаем вас пройти собеседование в компании");
        event.setDateStart(new Date());
//        event.setDateEnd();
        event.setDuration(new Duration.Builder()
                .hours(1)
                .build());

//        event.setOrganizer(new Organizer("Atomic Hunter", MailConfiguration.SERVICE_EMAIL));

//        Attendee a = new Attendee(consumerEmail, consumerEmail);
//        a.setParticipationLevel(ParticipationLevel.REQUIRED);
//        event.addAttendee(a);
        // сюда можно докинуть всех участников собрания
//        event.addAttendee(new Attendee("artemsrv3@gmail.com", "artemsrv3@gmail.com"));
        ical.addEvent(event);
        return Biweekly.write(ical).go();
    }

    public void sendInterviewInvite(String subject, String consumerEmail, Long vacancyRespondId) throws Exception {

        MimeMessage mimeMessage = emailSender.createMimeMessage();
        InternetAddress sender = new InternetAddress(serviceMail, "Atomic Hunter");
        InternetAddress consumer = new InternetAddress(consumerEmail);

//        mimeMessage.addHeaderLine("method=PUBLISH");
//        mimeMessage.addHeaderLine("charset=UTF-8");
//        mimeMessage.addHeaderLine("component=VEVENT");
        mimeMessage.setFrom(sender);
        mimeMessage.addRecipient(Message.RecipientType.TO, consumer);
//        mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(""));
        mimeMessage.setSubject(subject);

        StringBuilder builder = new StringBuilder();
        builder.append(generateICalDataForInvite(consumerEmail));

        MimeBodyPart messageBodyPart = new MimeBodyPart();

        messageBodyPart.setHeader("Content-Class", "urn:content-classes:calendarmessage");
        messageBodyPart.setHeader("Content-ID", "calendar_message");
        messageBodyPart.setDataHandler(new DataHandler(
                new ByteArrayDataSource(builder.toString(), "text/calendar;method=PUBLISH;name=\"invite.ics\"")));

        MimeBodyPart textPart = new MimeBodyPart();

        ConfirmationToken confirmationToken = this.createConfirmationTokenInterviewInvite(consumerEmail, vacancyRespondId);
        String html = resourceHelper.getResourceAsString(this.inviteMailHtml);

        if (this.serviceAddress == null) {
            this.serviceAddress = InetAddress.getLoopbackAddress().getHostAddress();
        }

        String address = "http://" + this.serviceAddress + ":" + runningPort +
                "/confirmation?token="
                + confirmationToken.getConfirmationToken();
        html = html.replaceAll("a href=\"#\"", String.format("a href=\"%s\"", serviceAddress));
        textPart.setContent(html + address, "text/html; charset=utf-8");

        MimeMultipart multipart = new MimeMultipart();

        multipart.addBodyPart(textPart);
        multipart.addBodyPart(messageBodyPart);

        mimeMessage.setContent(multipart);


        emailSender.send(mimeMessage);
        log.info("Invite for interview sent");
    }

    private ConfirmationToken createConfirmationTokenInterviewInvite(String email, Long vacancyRespondId) {
        ConfirmationToken confirmationToken = new ConfirmationToken(email, vacancyRespondId);
        confirmationToken = confirmationTokenRepository.save(confirmationToken);
        return confirmationToken;
    }
}