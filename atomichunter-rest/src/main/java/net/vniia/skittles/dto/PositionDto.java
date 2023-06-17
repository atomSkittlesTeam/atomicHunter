package net.vniia.skittles.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.vniia.skittles.entities.Position;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PositionDto {
    private String id;
    private String name;

    public PositionDto (Position position) {
        this.id = position.getId();
        this.name = position.getName();
    }
}
