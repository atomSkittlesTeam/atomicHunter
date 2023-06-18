import {Employee} from "./Employee";
import {CompetenceWeightScore} from "./CompetenceWeightScore";

export class VacancyCompetenceScoreRequestDto {
    interviewId: number;
    vacancyRespondId: number;
    employee: Employee;
    competenceWeightScoreList: CompetenceWeightScore[];
    comment: string;
    vacancyCompetenceId: number;
}