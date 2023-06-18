package net.vniia.skittles.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceDto {

    private Long id;

    // наименование места (например "404 комната")
    private String name;

    private boolean archive;

    private List<String> timeMap; //здесь будет список забронированных таймстемпов
}
