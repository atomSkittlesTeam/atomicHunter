package net.vniia.skittles.configs;

import net.vniia.skittles.dto.TelegramBotSettingsDto;
import net.vniia.skittles.dto.TelegramCommandDto;

import java.util.List;

public class TelegramBotSettings {

    public static String testUrl = "http://localhost:8080";
    public static String testingUrl = "http://localhost:8081";
    public static String testedUrl = "http://localhost:8082";

    public TelegramBotSettingsDto getBotSettings(String telegramLink, String telegramToken) {
        TelegramBotSettingsDto settingsDto = new TelegramBotSettingsDto();
        settingsDto.link = telegramLink;
        settingsDto.token = telegramToken;
        return settingsDto;
    }

    public static List<TelegramCommandDto> telegramCommands = List.of(
            new TelegramCommandDto("/help",
                    "Вызов доступных команд",
                    null, false, true),
            new TelegramCommandDto("/login",
                    "Залогиниться в чат-боте. Введите /login Логин Пароль' через пробел",
                    null, true, false)
//            new TelegramCommandDto("/user",
//                    "Команда, доступная всем ролям",
//                    null, false, true),
//            new TelegramCommandDto("/admin",
//                    "Команда, видимая и доступная только админу",
//                    List.of("admin"), false, true),
//            new TelegramCommandDto("/chief",
//                    "Команда, видимая и доступная админу и шефу",
//                    List.of("admin", "chief"), false, true)
    );


}

