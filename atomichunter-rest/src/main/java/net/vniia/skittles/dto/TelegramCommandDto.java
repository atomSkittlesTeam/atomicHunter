package net.vniia.skittles.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelegramCommandDto {
    private String command;
    private String description;
    private List<String> roles;
    private boolean isVisibleToNotAuthorized;
    private boolean isVisibleToAuthorized;
}