import { Position } from "./Position";
import { CompetenceWeight } from "./CompetenceWeight";

export class  Vacancy {
    id: number;
    position: Position;
    name: string;
    salary: string;
    experience: string;
    additional: string;
    archive: boolean;
    createInstant: Date;
    modifyInstant: Date;
    competenceWeight: CompetenceWeight[] = [];
}