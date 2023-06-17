import {CompetenceGroupDto} from "./CompetenceGroupDto";
import {Competence} from "./Competence";

export class CompetenceGroupsWithCompetencesDto {
    competenceGroup: CompetenceGroupDto;
    competences: Competence[];
}