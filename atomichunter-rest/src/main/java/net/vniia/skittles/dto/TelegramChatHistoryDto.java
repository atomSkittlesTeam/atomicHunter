package net.vniia.skittles.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.vniia.skittles.entities.TelegramChatHistory;

import java.time.Instant;

@Data
@NoArgsConstructor
public class TelegramChatHistoryDto {
    private Integer messageId;
    private Long userId;
    private String userName;
    private String message;
    private Instant messageDate;

    public TelegramChatHistoryDto(TelegramChatHistory telegramChatHistory) {
        this.messageId = telegramChatHistory.getMessageId();
        this.userId = telegramChatHistory.getUserId();
        this.userName = telegramChatHistory.getUserName();
        this.message = telegramChatHistory.getMessage();
        this.messageDate = telegramChatHistory.getMessageDate();
    }
}
