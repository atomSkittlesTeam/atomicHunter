package net.vniia.skittles.generators;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.services.MessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TelegramBotMailingGenerator {

    @Value("${generators.telegram}")
    private Boolean generatorEnable;

    private final MessageService messageService;

    @PostConstruct
    public void generateMailingMessages() {
        if (generatorEnable) {
            this.messageService.createTelegramMessageByLogin("admin", "Тест рассылки по логину");
            this.messageService.createTelegramMessageByRole("admin", "Тест рассылки по роли");
        }
    }
}
