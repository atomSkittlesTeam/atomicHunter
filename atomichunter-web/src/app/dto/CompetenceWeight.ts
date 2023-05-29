import { Competence } from "./Competence";

export class CompetenceWeight {
  competence: Competence;
  weight: number;

  constructor(competence: Competence, weight: number) {
    this.competence = competence;
    this.weight = weight;
  }
}