package net.vniia.skittles.dto;

import net.vniia.skittles.entities.TelegramSubscriber;
import net.vniia.skittles.entities.User;

import lombok.*;


@Data
@NoArgsConstructor
public class UserDto {
    public String login;
    private String fullName;
    private String email;
    private String role;
    private TelegramSubscriberDto telegramSubscriber;

    public UserDto(User user, TelegramSubscriber telegramSubscriber) {
        this.login = user.getLogin();
        this.role = user.getRole();
        this.email = user.getEmail();
        this.fullName = user.getFullName();
        if (telegramSubscriber != null) {
            this.telegramSubscriber = new TelegramSubscriberDto(telegramSubscriber);
        }
    }

    @Deprecated
    public UserDto UserDtoWithTelegram(User user, TelegramSubscriber telegramSubscriber) {
        this.login = user.getLogin();
        this.role = user.getRole();
        this.email = user.getEmail();
        this.fullName = user.getFullName();
        if (telegramSubscriber != null) {
            this.telegramSubscriber = new TelegramSubscriberDto(telegramSubscriber);
        }
        return this;
    }
}