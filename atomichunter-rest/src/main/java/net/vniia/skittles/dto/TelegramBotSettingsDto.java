package net.vniia.skittles.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@Data
@AllArgsConstructor

@NoArgsConstructor
public class TelegramBotSettingsDto {

    public String link;
    public String token;
}
