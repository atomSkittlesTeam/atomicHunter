package net.vniia.skittles.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.vniia.skittles.dto.CompetenceGroupDto;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompetenceGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    public CompetenceGroup(CompetenceGroupDto competenceGroupDto) {
        this.update(competenceGroupDto);
    }

    public void update(CompetenceGroupDto competenceGroupDto) {
        this.name = competenceGroupDto.getName();
    }
}
