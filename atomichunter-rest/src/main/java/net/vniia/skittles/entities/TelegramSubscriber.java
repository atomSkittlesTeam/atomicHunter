package net.vniia.skittles.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.vniia.skittles.dto.TelegramSubscriberDto;
import net.vniia.skittles.dto.UserDto;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "telegram_subscribers")
public class TelegramSubscriber {
    @Id
    @Column(nullable = false, unique = true)
    public String login;
    public String fullName;
    private Long telegramId;
    private String telegramName;

    @JoinTable(
            name = "authorities",
            joinColumns = {@JoinColumn(name = "login")})
    @Column(name = "authority")
    private String role;

    public void update(TelegramSubscriberDto subscriberDto) {
        this.login = subscriberDto.getLogin();
        this.fullName = subscriberDto.getFullName();
        this.telegramId = subscriberDto.getTelegramId();
        this.telegramName = subscriberDto.getTelegramName();
        this.role = subscriberDto.getRole();
    }
}
