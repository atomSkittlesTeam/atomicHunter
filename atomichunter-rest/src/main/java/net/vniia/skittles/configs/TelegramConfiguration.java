package net.vniia.skittles.configs;

import lombok.extern.log4j.Log4j2;
import net.vniia.skittles.controllers.TelegramBotController;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@Log4j2
public class TelegramConfiguration {

    @Value("${telegram.link}")
    private String telegramLink;

    @Autowired
    private TelegramBotController telegramBotController;

    @Bean
    public void telegramBotConstruct() throws TelegramApiException {
        if (!telegramLink.equals("null")) {
            try {
                TelegramBotController.telegramBotIsEnabled = true;
                TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
                botsApi.registerBot(telegramBotController);
            } catch (Exception exception) {
                System.out.println("Телеграм бот не может быть активирован! Проверьте подключение к интернету. " +
                        "Также возможно, настройки блокируют telegram-api");
            }
        } else {
            log.warn("Телеграм-бот не включен! Скорее всего, не проставлены настройки TelegramLink, TelegramToken");
        }
    }
}
