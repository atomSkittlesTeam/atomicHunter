import {Interview} from "./Interview";
import {Vacancy} from "./Vacancy";

export class InterviewCalendarDto {
    interview: Interview;
    vacancy: Vacancy;
    members: string;
}