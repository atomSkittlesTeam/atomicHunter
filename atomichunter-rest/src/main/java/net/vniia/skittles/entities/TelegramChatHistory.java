package net.vniia.skittles.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "telegram_chat_history")
public class TelegramChatHistory {
    @Id
    private Integer messageId;
    private Long userId;
    private String userName;
    private String message;
    private Instant messageDate;
}
