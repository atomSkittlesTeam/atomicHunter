package net.vniia.skittles.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.vniia.skittles.enums.TelegramMailingType;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "telegram_mailing_history")
public class TelegramMailingHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private TelegramMailingType mailingType;
    private String mailingTo;
    private String message;
    private Instant messageDate;
    private Boolean processed;
}
