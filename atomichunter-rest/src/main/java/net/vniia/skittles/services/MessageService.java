package net.vniia.skittles.services;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.MessageDto;
import net.vniia.skittles.dto.UserDto;
import net.vniia.skittles.entities.*;
import net.vniia.skittles.enums.TelegramMailingType;
import net.vniia.skittles.repositories.MessageRepository;
import net.vniia.skittles.repositories.TelegramMailingHistoryRepository;
import net.vniia.skittles.repositories.TelegramSubscriberRepository;
import net.vniia.skittles.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    @Value("${telegram.link}")
    private String telegramLink;

    @Value("${telegram.token}")
    private String telegramToken;

    private final MessageRepository messageRepository;

    private final JPAQueryFactory queryFactory;

    private final EmailServiceImpl emailService;

    private final UserService userService;

    private final UserRepository userRepository;

    private final TelegramSubscriberRepository telegramSubscriberRepository;

    private final TelegramMailingHistoryRepository telegramMailingHistoryRepository;

    public static final QMessage message = QMessage.message;

    @Transactional
    public void sendMessageOfNewRequests() {
//        List<Message> messages = messageRepository.findAll();
//        List<Message> newMessages = messages.stream().filter(e -> e.getEmailSign().equals(false)
//                && e.getType().equals(Types.newRequests)).toList();
//        String numbers = String.join(",", newMessages.stream().map(Message::getObjectName).toList());
//        List<String> emails = userService.getEmailsByRole("chief");
//        emails.forEach(email -> {
//            emailService.sendSimpleMessage(email,
//                    "Поступили новые заказы",
//                    ("Количество заказов: " + newMessages.size() + "\n"
//                            + "Номера заказов: " + numbers));
//        });
//        newMessages.forEach(e -> e.setEmailSign(true));
//        messageRepository.saveAll(newMessages);
//        System.out.println("Отправил сообщение о новых реквестах");
    }

    public List<MessageDto> getNewMessagesForFrontendByLogin(String userLogin) {
        List<Message> newMessages = queryFactory
                .from(message)
                .select(message)
                .where(message.login.eq(userLogin)
                        .and(message.frontSign.eq(false)
                                .or(message.frontSign.isNull())))
                .fetch();
        return newMessages.stream().map(MessageDto::new).toList();
    }

    @Transactional
    public void setMessageReadFromFrontend(List<Long> messageIds) {
        List<Message> messages = messageRepository.findAllById(messageIds);
        messages.forEach(e -> e.setFrontSign(true));
        messageRepository.saveAll(messages);
        messageRepository.flush();
    }

    @Scheduled(fixedDelay = 10000)
    public void scheduledSendEmailMessages() {
        List<Message> newMessages = queryFactory.select(message)
                .from(message)
                .where(message.emailSign.isFalse())
                .fetch();

        newMessages.forEach(message -> {
            UserDto user = userService.getUser(message.getLogin());
            emailService.sendSimpleMessage(user.getEmail(), message.getObjectName(), message.getObjectName());
        });

        newMessages.forEach(e -> e.setEmailSign(true));
        messageRepository.saveAll(newMessages);
//        System.out.println("Отправил сообщения на почты, по шедулеру");
    }

    public Message createMessageByLogin(String login, String messageText, Long objectId) {
        User user = userRepository.findByLogin(login);
        if (user != null) {
            Message message = new Message(user.login,
                    false,
                    false,
                    objectId,
                    messageText);
            message = messageRepository.save(message);
            return message;
        } else {
            throw new RuntimeException("User не найден");
        }
    }

    public List<Message> createMessagesByRole(String role, Long objectId, String messageText) {
        List<Message> messageList = new ArrayList<>();
        List<User> actualUsers = userRepository.findAllByRole(role);
        for (User user : actualUsers) {
            Message message = new Message(user.login, false, false, objectId, messageText);
            messageList.add(message);
        }
        messageRepository.saveAll(messageList);
        return messageList;
    }

    public List<Message> createMessagesForAllUsers(Long objectId, String messageText) {
        List<Message> messageList = new ArrayList<>();
        List<User> actualUsers = userRepository.findAll();
        for (User user : actualUsers) {
            Message message = new Message(user.login, false, false, objectId, messageText);
            messageList.add(message);
        }
        messageList = messageRepository.saveAll(messageList);
        return messageList;
    }

    public void createTelegramMessageByRole(String role, String message) {
        if (isTelegramEnabled()) {
            List<TelegramSubscriber> usersWithRole = telegramSubscriberRepository.findAllByRole(role);
            if (!usersWithRole.isEmpty()) {
                List<TelegramMailingHistory> historiesForRoleMailing = new ArrayList<>();
                for (TelegramSubscriber user : usersWithRole) {
                    TelegramMailingHistory mailToUser = new TelegramMailingHistory();
                    mailToUser.setMailingType(TelegramMailingType.ROLE);
                    mailToUser.setMailingTo(user.getRole());
                    mailToUser.setMessage(message);
                    mailToUser.setMessageDate(Instant.now());
                    mailToUser.setProcessed(false);
                    historiesForRoleMailing.add(mailToUser);
                }
                telegramMailingHistoryRepository.saveAll(historiesForRoleMailing);
            }
        }
    }

    public void createTelegramMessageByLogin(String login, String message) {
        if (isTelegramEnabled()) {
            TelegramSubscriber subscriber = this.telegramSubscriberRepository.findByLogin(login);
            if (subscriber != null) {
                TelegramMailingHistory mailToSubscriber = new TelegramMailingHistory();
                mailToSubscriber.setMailingType(TelegramMailingType.LOGIN);
                mailToSubscriber.setMailingTo(subscriber.getLogin());
                mailToSubscriber.setMessage(message);
                mailToSubscriber.setMessageDate(Instant.now());
                mailToSubscriber.setProcessed(false);
                this.telegramMailingHistoryRepository.save(mailToSubscriber);
            }
        }
    }

    public void createTelegramMessagesForAllUsers(String message) {
        if (isTelegramEnabled()) {
            List<TelegramSubscriber> subscribers = this.telegramSubscriberRepository.findAll();
            if (!subscribers.isEmpty()) {
                List<TelegramMailingHistory> messages = new ArrayList<>();
                for (TelegramSubscriber subscriber : subscribers) {
                    TelegramMailingHistory mailToSubscriber = new TelegramMailingHistory();
                    mailToSubscriber.setMailingType(TelegramMailingType.LOGIN);
                    mailToSubscriber.setMailingTo(subscriber.getLogin());
                    mailToSubscriber.setMessage(message);
                    mailToSubscriber.setMessageDate(Instant.now());
                    mailToSubscriber.setProcessed(false);
                    messages.add(mailToSubscriber);
                }
                this.telegramMailingHistoryRepository.saveAll(messages);
            }
        }
    }

    private boolean isTelegramEnabled() {
        return !StringUtils.isEmpty(this.telegramLink) && !StringUtils.isEmpty(this.telegramToken)
                && !this.telegramLink.equals("null") && !this.telegramToken.equals("null");
    }
}
