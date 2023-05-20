package net.vniia.skittles.configs;

import net.vniia.skittles.dto.TelegramBotSettingsDto;
import net.vniia.skittles.dto.TelegramCommandDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TelegramBotConfig {

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
                    null, true, true),
            new TelegramCommandDto("/login",
                    "Залогиниться в чат-боте. Введите /login Логин Пароль' через пробел",
                    null, true, false),
            new TelegramCommandDto("/joke",
                    "Показать юмореску",
                    null, false, true),
            new TelegramCommandDto("/user",
                    "Команда, доступная всем ролям",
                    null, false, true),
            new TelegramCommandDto("/admin",
                    "Команда, видимая и доступная только админу",
                    List.of("admin"), false, true),
            new TelegramCommandDto("/chief",
                    "Команда, видимая и доступная админу и шефу",
                    List.of("admin", "chief"), false, true)
    );

    //deprecated
//    public static Map<String, String> commandsWithDescription = new HashMap<>(
//            Map.of("/help", "Вызов доступных команд",
//                    "/login", "Залогиниться в чат-боте. Введите /login Логин Пароль' через пробел",
//                    "/joke", "Показать юмореску",
//                    "/test", "Введите '/test число' через запятую. Затем ничего не произойдет"
//                    ));



}

