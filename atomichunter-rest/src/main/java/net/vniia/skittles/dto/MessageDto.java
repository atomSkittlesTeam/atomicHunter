package net.vniia.skittles.dto;

import net.vniia.skittles.entities.Message;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class MessageDto {
    private Long id;
    private String login;
    private Boolean emailSign;
    private Boolean frontSign;
    private Long objectId; //либо request, либо machine
    private String objectName; //для request - number
    private Instant instant; //дата появления сообщения
    private String customText; //вдруг че пилить надо будет вручную

    public MessageDto(Message message) {
        this.id = message.getId();
        this.login = message.getLogin();
        this.emailSign = message.getEmailSign();
        this.frontSign = message.getFrontSign();
        this.objectId = message.getObjectId();
        this.objectName = message.getObjectName();
        this.instant = message.getInstant();
        this.customText = message.getObjectName();
//        this.customText = "Новая заявка! Номер заявки: " + message.getObjectName();
    }
}
