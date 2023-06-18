import { Competence } from "./Competence";

export class CompetenceWeightScore {
    competence: Competence;
    weight: number;
    score: number;
    binaryIsChecked: boolean;

    constructor(competence: Competence, weight: number, score: number, binaryIsCheched: boolean) {
        this.competence = competence;
        this.weight = weight;
        this.score = score;
        this.binaryIsChecked = binaryIsCheched;
    }
}