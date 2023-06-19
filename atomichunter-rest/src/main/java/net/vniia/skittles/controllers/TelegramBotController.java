package net.vniia.skittles.controllers;

import net.vniia.skittles.configs.TelegramBotSettings;
import net.vniia.skittles.entities.TelegramSubscriber;
import net.vniia.skittles.services.TelegramBotService;
import net.vniia.skittles.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

@Component
public class TelegramBotController extends TelegramLongPollingBot {

    @Value("${telegram.link}")
    private String telegramLink;

    @Value("${telegram.token}")
    private String telegramToken;

    @Value("${scheduled.telegram-bot-mailing}")
    private String telegramBotMailingDelay;

    public static boolean telegramBotIsEnabled = false;

    private final RestTemplate restTemplate = new RestTemplate();

    private final TelegramBotSettings telegramBotSettings = new TelegramBotSettings();

    @Autowired
    UserService userService;

    @Autowired
    TelegramBotService telegramBotService;

    @Override
    public String getBotUsername() {
        return "TestBot";
    }

    @Override
    public String getBotToken() {
        return telegramBotSettings.getBotSettings(telegramLink, telegramToken).getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();

        if (message != null && message.hasText()) {
            TelegramSubscriber subscriber = userService.getTelegramSubscriberById(message.getFrom().getId());
            Boolean userIsNotAuthorized = (subscriber == null);
            if (message.getText().contains("/login")) {
                String response = telegramBotService.commandLoginToTelegramAndEncodePasswordForLogging(message);
                sendMsg(message, response);
            } else {
                switch (message.getText()) {
                    case "/start" -> {
                        String response = telegramBotService.commandShowStart(message, userIsNotAuthorized, subscriber);
                        sendMsg(message, response);
                    }
                    case "/help" -> {
                        String response = telegramBotService.commandShowHelp(message, userIsNotAuthorized, subscriber);
                        sendMsg(message, response);
                    }
//                    case "/user" -> {
//                        String response = telegramBotService.commandUser(message, subscriber);
//                        sendMsg(message, response);
//                    }
//                    case "/chief" -> {
//                        String response = telegramBotService.commandChief(message, subscriber);
//                        sendMsg(message, response);
//                    }
//                    case "/admin" -> {
//                        String response = telegramBotService.commandAdmin(message, subscriber);
//                        sendMsg(message, response);
//                    }
                    default -> {
                        String response = telegramBotService.commandDefault(message);
                        sendMsg(message, response);
                    }
                }
            }
        }

    }

    public void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString()); // Определение в какой чат отправить ответ
        // Можно сделать определение, на какое сообщение отвечать
        sendMessage.setText(text);
        try {
//            setButtons(sendMessage);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMsgByRole(String role, String text) {
        if(role.split("=").length > 1) {
            role = role.split("=")[1];
        }
        List<TelegramSubscriber> telegramSubscribers = userService.getTelegramSubscribersByRole(role);
        if (telegramSubscribers.isEmpty()) {
            throw new RuntimeException("Не найдено ни одного подписчика с такой ролью");
        }
        for (TelegramSubscriber subscriber : telegramSubscribers) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.enableMarkdown(true);
            sendMessage.setChatId(subscriber.getTelegramId().toString());
            sendMessage.setText(text);
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                System.out.println("Не найдено такого пользователя");
            }
        }
    }

    public void sendMsgByLogin(String login, String text) {
        if(login.split("=").length > 1) {
            login = login.split("=")[1];
        }
        TelegramSubscriber telegramSubscriber = userService.getTelegramSubscriberByLogin(login);
        if (telegramSubscriber == null) {
            throw new RuntimeException("Не найден такой подписчик");
        }
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(telegramSubscriber.getTelegramId().toString());
        sendMessage.setText(text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void inputNumber(Message message) {
        List<String> messageParts = Arrays.stream(message.getText().split(" ")).toList();
        try {
            Integer number = Integer.valueOf(messageParts.get(1));
            String result = "Произошло ничего";
            sendMsg(message, result + "\n");
        } catch (NumberFormatException exception) {
            //неправильный формат введенных строчек
            sendMsg(message, "Команда введена неправильно\n");
        }
    }

    public void telegramUnsubscribe(String login) {
        //todo вообще тут может упасть удаление из таблицы, а в чатике всё равно будет сообщение
        sendMsgByLogin(login, "Вы отписаны от канала");
        userService.telegramUnsubscribe(login);
    }

    @Scheduled(fixedDelay = 20000)
    public void mailingByTableMailingHistory() {
        if (telegramBotIsEnabled) {
            Map<String, String> mapMessagesByLogins = telegramBotService.getMailingByLoginQueue();
            Map<String, String> mapMessagesByRoles = telegramBotService.getMailingByRoleQueue();
            mapMessagesByLogins.forEach(this::sendMsgByLogin);
            mapMessagesByRoles.forEach(this::sendMsgByRole);
        }
    }
}
