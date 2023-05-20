package net.vniia.skittles.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.vniia.skittles.entities.TelegramSubscriber;
import net.vniia.skittles.entities.User;


@Data
@NoArgsConstructor
public class PasswordChangeRequestDto {
    public String oldPassword;
    public String newPassword;
    public UserDto user;
}