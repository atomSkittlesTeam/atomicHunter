package net.vniia.skittles.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private String login; //логин пользователя, которому приходит этот message
    private Boolean emailSign;
    private Boolean frontSign;
    private Long objectId; //либо request, либо machine
    private String objectName; //для request - number
    private Instant instant; //дата появления сообщения

    public Message(String login, Boolean emailSign, Boolean frontSign, Long objectId, String objectName) {
        this.login = login;
        this.emailSign = emailSign;
        this.frontSign = frontSign;
        this.objectId = objectId;
        this.objectName = objectName;
        this.instant = Instant.now();
    }
}
