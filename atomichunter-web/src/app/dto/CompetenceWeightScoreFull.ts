import {Competence} from "./Competence";
import {VacancyRespond} from "./VacancyRespond";

export class CompetenceWeightScoreFull {
    competence: Competence;
    vacancyRespond: VacancyRespond;
    vacancyCompetenceId: number;
    weight: number;
    score: number;
    comment: string;
    employeeId: string;
}