package net.vniia.skittles.dto;

import net.vniia.skittles.entities.Request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Deprecated
public class RequestDto {
    private Long id;
    private String number;
    private Date requestDate;
    private String description;
    private Date releaseDate;//в базе int, а тут флоат - для нормального отображения часов
    private Long priority;
    private boolean archive;
}