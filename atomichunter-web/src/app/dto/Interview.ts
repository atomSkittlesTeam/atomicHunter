import { VacancyRespond } from "./VacancyRespond";
import {Employee} from "./Employee";
import {Place} from "./Place";

export class Interview {
    id: number;
    vacancyRespond: VacancyRespond;
    place: Place;
    dateStart: Date;
    dateEnd: Date;
    employees: Employee[];
}