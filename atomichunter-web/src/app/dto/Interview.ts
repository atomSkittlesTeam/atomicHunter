import { VacancyRespond } from "./VacancyRespond";
import {Employee} from "./Employee";

export class Interview {
    id: number;
    vacancyRespond: VacancyRespond;
    meeting: string;
    dateStart: Date;
    dateEnd: Date;
    employees: Employee[];
}