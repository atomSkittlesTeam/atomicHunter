import {Position} from "./Position";
import {Employee} from "./Employee";
import {StatusEnum} from "./status-enum";

export class StaffUnitDto {
    id: string;
    positionId: string;
    employeeId: string;
    employee: Employee;
    status: StatusEnum;
    position: Position;
    closeTime: Date;
}
