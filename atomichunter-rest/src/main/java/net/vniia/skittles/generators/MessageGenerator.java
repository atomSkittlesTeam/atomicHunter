package net.vniia.skittles.generators;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.services.MessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageGenerator {

    @Value("${generators.message}")
    private Boolean generatorEnable;

    private final MessageService messageService;

    @PostConstruct
    public void generateMailingMessages() {
        if (generatorEnable) {
            generateMessagesByRole("admin");
            generateMessagesByRole("user");
            generateMessageByLogin("admin");
            generateMessageByLogin("chief");
        }
    }

    private void generateMessagesByRole(String role) {
        messageService.createMessagesByRole(role, 1L,
                "Генерация по роли! \n " +
                        "Это тест показа messages. " +
                        "Он генерируется при включенной переменной generators.message");
    }

    private void generateMessageByLogin(String login) {
        messageService.createMessageByLogin(login,
                "Генерация по логину! \n Это тест показа messages. " +
                        "Он генерируется при включенной переменной generators.message",
                1L);
    }
}
