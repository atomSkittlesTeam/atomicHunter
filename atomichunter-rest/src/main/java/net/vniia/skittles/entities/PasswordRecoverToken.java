package net.vniia.skittles.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Random;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordRecoverToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String simpleConfirmationToken;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Instant createInstant;

    private String email;

    private boolean accepted;

    public PasswordRecoverToken(String email) {
        Random random = new Random();
        this.simpleConfirmationToken = String.format("%06d", random.nextInt(0, 999999));
        this.email = email;
    }
}
