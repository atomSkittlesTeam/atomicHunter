import { Competence } from "./Competence";

export class CompetenceWeight {
  competence: Competence;
  weight: number;
  binaryLogic: boolean;

  constructor(competence: Competence, weight: number, binaryLogic: boolean) {
    this.competence = competence;
    this.weight = weight;
    this.binaryLogic = binaryLogic;
  }
}