package net.vniia.skittles.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private UUID confirmationToken;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Instant createInstant;

    private String email;

    private Long vacancyRespondId;

    private boolean accepted;

    public ConfirmationToken(String email, Long vacancyRespondId) {
        this.confirmationToken = UUID.randomUUID();
        this.email = email;
        this.vacancyRespondId = vacancyRespondId;
    }
}
