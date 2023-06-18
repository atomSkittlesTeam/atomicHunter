package net.vniia.skittles.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.vniia.skittles.dto.CompetenceDto;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Competence {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private boolean binaryLogic;

    private Long groupId;

    public Competence(Long groupId, CompetenceDto competenceDto) {
        this.name = competenceDto.getName();
        this.groupId = groupId;
        this.binaryLogic = competenceDto.isBinaryLogic();
    }

    public void update(CompetenceDto competenceDto) {
        this.name = competenceDto.getName();
        this.binaryLogic = competenceDto.isBinaryLogic();
    }
}
