package net.vniia.skittles.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.vniia.skittles.entities.TelegramMailingHistory;
import net.vniia.skittles.enums.TelegramMailingType;

import java.time.Instant;

@Data
@NoArgsConstructor
public class TelegramMailingHistoryDto {
    private Long id;
    private TelegramMailingType mailingType;
    private String mailingTo;
    private String message;
    private Instant messageDate;

    public TelegramMailingHistoryDto(TelegramMailingHistory telegramMailingHistory) {
        this.id = telegramMailingHistory.getId();
        this.mailingType = telegramMailingHistory.getMailingType();
        this.mailingTo = telegramMailingHistory.getMailingTo();
        this.message = telegramMailingHistory.getMessage();
        this.messageDate = telegramMailingHistory.getMessageDate();
    }
}
