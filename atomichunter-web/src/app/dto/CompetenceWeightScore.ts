import { Competence } from "./Competence";

export class CompetenceWeightScore {
    competence: Competence[];
    weight: number;
    score: number;

    constructor(competence: Competence[], weight: number, score: number) {
        this.competence = competence;
        this.weight = weight;
        this.score = score;
    }
}