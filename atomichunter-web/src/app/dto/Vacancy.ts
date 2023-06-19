import { Position } from "./Position";
import { CompetenceWeight } from "./CompetenceWeight";
import {StaffUnitDto} from "./StaffUnitDto";
import {Employee} from "./Employee";

export class  Vacancy {
    id: number;
    name: string;
    // штатка
    staffUnit: StaffUnitDto;
    // должность
    position: Position;
    // описание требований к кандидату
    requirements: string;
    // описание обязанностей
    responsibilities: string;
    // предлагаемые условия работы
    conditions: string;
    // сотрудник кадровой службы ответственный за вакансию (join с employId)
    hr: Employee;
    archive: boolean;
    closed: boolean;
    createInstant: Date;
    modifyInstant: Date;
    competenceWeight: CompetenceWeight[] = [];
}