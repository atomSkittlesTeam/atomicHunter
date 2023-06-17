package net.vniia.skittles.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompetenceGroupsWithCompetencesDto {
    private CompetenceGroupDto competenceGroup;
    private List<CompetenceDto> competences;
}
