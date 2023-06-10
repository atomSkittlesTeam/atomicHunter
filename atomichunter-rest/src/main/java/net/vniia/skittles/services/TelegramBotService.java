package net.vniia.skittles.services;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.configs.TelegramBotSettings;
import net.vniia.skittles.dto.TelegramCommandDto;
import net.vniia.skittles.entities.*;
import net.vniia.skittles.enums.TelegramMailingType;
import net.vniia.skittles.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TelegramBotService {

    @Autowired
    UserService userService;

    @Autowired
    TelegramSubscriberRepository telegramSubscriberRepository;

    @Autowired
    TelegramChatHistoryRepository telegramChatHistoryRepository;

    @Autowired
    TelegramMailingHistoryRepository telegramMailingHistoryRepository;

    private QTelegramChatHistory telegramChatHistory =
            new QTelegramChatHistory("telegramChatHistory");
    private QTelegramMailingHistory telegramMailingHistory =
            new QTelegramMailingHistory("telegramMailingHistory");

    @Autowired
    private JPAQueryFactory queryFactory;

    public String commandDefault(Message message) {
        loggingAndSavingChatHistory(standardMessageToLogging(message), message, null);
        return "Команда не распознана\nСписок доступных команд (/help)";
    }

    public String commandShowHelp(Message message, Boolean userIsNotAuthorized, TelegramSubscriber subscriber) {
        StringBuilder messageToTelegramChat = new StringBuilder();
        List<TelegramCommandDto> actualCommands = TelegramBotSettings.telegramCommands;
        if(userIsNotAuthorized) {
            actualCommands = actualCommands.stream().filter(e -> e.isVisibleToNotAuthorized()).toList();
        } else {
            actualCommands = actualCommands.stream().filter(e -> e.isVisibleToAuthorized()
                    && (e.getRoles() == null || e.getRoles().contains(subscriber.getRole()))).toList();
        }
        actualCommands.forEach(command ->
                messageToTelegramChat.append(command.getCommand()).append(" - ").append(command.getDescription()).append("\n")
        );
        loggingAndSavingChatHistory(standardMessageToLogging(message), message, null);
        return "Вам доступны команды: \n" + messageToTelegramChat;
    }

    public String commandLoginToTelegramAndEncodePasswordForLogging(Message message) {
        String messageToTelegramChat; //полетит в чат для пользователя
        String messageToLogging; //полетит в лог
        String messageToSaving; //полетит в БД
        if (!loginTemplateIsCorrect(message)) {
            messageToTelegramChat = "Не соблюден шаблон логина!\n Введите '/login Логин Пароль' через пробел";
        } else {
            String userLogin = message.getText().split(" ")[1];
            String userPassword = message.getText().split(" ")[2];
            messageToTelegramChat = userService.loginToTelegramBotOrShowError(userLogin, userPassword,
                    message.getFrom().getUserName(), message.getFrom().getId());
            messageToSaving = message.getText().split(" ")[0] + " "
                    + message.getText().split(" ")[1] + " ##$#%!*#*%@#";
            messageToLogging = "Id: " + message.getFrom().getId()
                    + " userName: " + message.getFrom().getUserName()
                    + " message: " + messageToSaving;
            loggingAndSavingChatHistory(messageToLogging, message, messageToSaving);
        }
        return messageToTelegramChat;
    }

    public String commandUser(Message message, TelegramSubscriber subscriber) {
        String messageToTelegramChat;
        messageToTelegramChat = checkCommandRoleMappingReturnErrorOrNull(message.getText(), subscriber);
        if (messageToTelegramChat == null) {
            messageToTelegramChat = "Команда юзера работает!";
        }
        loggingAndSavingChatHistory(standardMessageToLogging(message), message, null);
        return messageToTelegramChat;
    }

    public String commandChief(Message message, TelegramSubscriber subscriber) {
        String messageToTelegramChat;
        messageToTelegramChat = checkCommandRoleMappingReturnErrorOrNull(message.getText(), subscriber);
        if (messageToTelegramChat == null) {
            messageToTelegramChat = "Команда шефа работает!";
        }
        loggingAndSavingChatHistory(standardMessageToLogging(message), message, null);
        return messageToTelegramChat;
    }

    public String commandAdmin(Message message, TelegramSubscriber subscriber) {
        String messageToTelegramChat;
        messageToTelegramChat = checkCommandRoleMappingReturnErrorOrNull(message.getText(), subscriber);
        if (messageToTelegramChat == null) {
            messageToTelegramChat = "Команда админа работает!";
        }
        loggingAndSavingChatHistory(standardMessageToLogging(message), message, null);
        return messageToTelegramChat;
    }

    private String checkCommandRoleMappingReturnErrorOrNull(String commandFromInput, TelegramSubscriber subscriber) {
        List<TelegramCommandDto> commandLikeList =
                TelegramBotSettings.telegramCommands.stream().filter(e -> e.getCommand().equals(commandFromInput)).toList();
        if (commandLikeList.isEmpty()) {
            throw new RuntimeException("Команда не найдена в конфиге");
        }
        TelegramCommandDto commandDto = commandLikeList.get(0);
        if (subscriber == null || //пользователь неавторизован
                subscriber.getRole() == null || //пользователь не имеет никакой роли (не должно происходить)
                (commandDto.getRoles() != null //команда имеет какую-либо ролевую модель, а не доступна вообще всем ролям
                        && !commandDto.getRoles().contains(subscriber.getRole()))) { //у пользователя нет нужных прав
            return "Команда не распознана\nСписок доступных команд (/help)";
        } else {
            return null;
        }
    }

    private Boolean loginTemplateIsCorrect(Message message) {
        if (message.getText().split(" ").length != 3) {
            return false;
        } else {
            return true;
        }
    }

    private void loggingAndSavingChatHistory(String messageToLogging, Message message, String messageOverride) {
        System.out.println(messageToLogging);
        TelegramChatHistory telegramChatHistory = new TelegramChatHistory();
        telegramChatHistory.setMessageId(message.getMessageId());
        telegramChatHistory.setMessage(messageOverride != null ? messageOverride : message.getText());
        telegramChatHistory.setUserId(message.getFrom().getId());
        telegramChatHistory.setUserName(message.getFrom().getUserName());
        telegramChatHistory.setMessageDate(Instant.now());
        telegramChatHistoryRepository.save(telegramChatHistory);
    }

    private String standardMessageToLogging(Message message) {
        return  "Id: " + message.getFrom().getId()
                + " userName: " + message.getFrom().getUserName()
                + " message: " + message.getText();
    }


    ///////// mailing here ////////
    public Map<String, String> getMailingByLoginQueue() {
        //todo может произойти процессед, а сообщение не отправится
        Map<String, String> mailingByLoginMap = new HashMap<>();
        List<TelegramMailingHistory> mailingList = (List<TelegramMailingHistory>) queryFactory.from(telegramMailingHistory)
                .where(telegramMailingHistory.processed.isFalse()
                        .and(telegramMailingHistory.mailingType.eq(TelegramMailingType.LOGIN))).fetch();
        mailingList.forEach(m -> {
            mailingByLoginMap.put(m.getId() + "=" + m.getMailingTo(), m.getMessage());
            m.setProcessed(true);
        });
        telegramMailingHistoryRepository.saveAllAndFlush(mailingList);
        return mailingByLoginMap;
    }

    public Map<String, String> getMailingByRoleQueue() {
        //todo может произойти процессед, а сообщение не отправится
        Map<String, String> mailingByRoleMap = new HashMap<>();
        List<TelegramMailingHistory> mailingList = (List<TelegramMailingHistory>) queryFactory.from(telegramMailingHistory)
                .where(telegramMailingHistory.processed.isFalse()
                        .and(telegramMailingHistory.mailingType.eq(TelegramMailingType.ROLE))).fetch();
        mailingList.forEach(m -> {
            mailingByRoleMap.put(m.getId() + "=" + m.getMailingTo(), m.getMessage());
            m.setProcessed(true);
        });
        telegramMailingHistoryRepository.saveAllAndFlush(mailingList);
        return mailingByRoleMap;
    }

}
