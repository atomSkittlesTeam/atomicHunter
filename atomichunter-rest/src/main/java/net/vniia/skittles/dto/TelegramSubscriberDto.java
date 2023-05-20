package net.vniia.skittles.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.vniia.skittles.entities.TelegramSubscriber;
import net.vniia.skittles.entities.User;


@Data
@NoArgsConstructor
public class TelegramSubscriberDto {
    public String login;
    public String fullName;
    private Long telegramId;
    private String telegramName;
    private String role;

    public TelegramSubscriberDto(TelegramSubscriber subscriber) {
        this.login = subscriber.getLogin();
        this.fullName = subscriber.getFullName();
        this.role = subscriber.getRole();
        this.telegramId = subscriber.getTelegramId();
        this.telegramName = subscriber.getTelegramName();
    }
}