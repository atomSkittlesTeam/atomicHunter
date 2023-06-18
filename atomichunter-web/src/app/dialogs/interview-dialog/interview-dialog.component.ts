import {Component, EventEmitter, Input, Output} from "@angular/core";
import {Interview} from "../../dto/Interview";
import {InterviewService} from "../../services/interview.service";
import {MessageService} from "primeng/api";
import {VacancyRespond} from "../../dto/VacancyRespond";
import {Employee} from "../../dto/Employee";
import {OrgStructService} from "../../services/org-struct.service";
import {Place} from "../../dto/Place";
import {PlaceService} from "../../services/place.service";

@Component({
    selector: "app-interview-dialog",
    templateUrl: "./interview-dialog.component.html",
    styleUrls: ["./interview-dialog.component.scss"]
})
export class VacancyRequestComponent {

    @Input("openDialog") visible: boolean = false;
    @Input("item") interview: Interview = new Interview();
    @Input("selectedVacancyRespond") selectedVacancyRespond: VacancyRespond = new VacancyRespond();

    @Input("editMode") editMode: boolean;
    @Output() submit = new EventEmitter<any>();
    @Output() visibleChange = new EventEmitter<any>();
    dialogTitle = "Организация собеседования";
    showSidebarWithAllSkills: boolean = false;
    employees: Employee[] = [];
    selectedEmployees: Employee[] = [];
    allPlaces: Place[] = [];


    constructor(private inviteService: InterviewService,
                private orgStructService: OrgStructService,
                private messageService: MessageService,
                private placeService: PlaceService) {
    }

    async ngOnInit() {
        if (this.selectedVacancyRespond.interviewId) {
            this.interview = await this.inviteService.getInterviewById(this.selectedVacancyRespond.interviewId);
            if (typeof this.interview.dateEnd === "number") {
                this.interview.dateEnd = new Date(this.interview.dateEnd * 1000);
            }
            if (this.interview?.employees) {
                this.selectedEmployees = this.interview.employees;
            }

            if (typeof this.interview.dateStart === "number") {
                this.interview.dateStart = new Date(this.interview.dateStart * 1000);
            }
            // new Date(data.value * 1000).toLocaleDateString()
            // + ' ' + new Date(data.value * 1000).toLocaleTimeString() : '';

            this.dialogTitle = "Редактирование собеседования";
        }

        try {
            this.allPlaces = await this.placeService.getPlaces(false);
        } catch (e) {
            this.messageService.add({
                severity: 'error',
                summary: 'Ошибка!',
                detail: 'Ошибка в поиске мест проведения собеседований',
                life: 5000
            });
        }
        if (this.allPlaces.length === 0) {
            this.messageService.add({
                severity: 'info',
                summary: 'Пустой справочник',
                detail: 'Справочник мест собеседований не заполнен',
                life: 5000
            });
        }
    }

    placeSaveToObject() {
        console.log(this.interview);
        // console.log(this.interview.place);
    }

    async openSidebarWithAllSkills() {
        this.showSidebarWithAllSkills = true;
        this.employees = await this.orgStructService.getEmployees();
        // this.competencesAll = await this.competenceService.getAllCompetences();

    }

    closeDialog($event?: any) {
        this.submit.emit($event);
        this.visible = false;
    }

    async onSubmit($event?: any) {
        this.interview.employees = this.selectedEmployees;
        try {
            if (this.selectedEmployees) {
                this.interview.employees = this.selectedEmployees;
            }

            if (this.editMode) {
                await this.inviteService.updateInterview(this.selectedVacancyRespond.interviewId, this.interview);
                this.messageService.add({
                    severity: "success",
                    summary: "Успех!",
                    detail: "Собеседование обновлено!",
                    life: 5000
                });
            } else {
                await this.inviteService.createInterview(this.selectedVacancyRespond.id, this.interview);
                this.messageService.add({
                    severity: "success",
                    summary: "Успех!",
                    detail: "Собеседование создано!",
                    life: 5000
                });
            }

        } catch (e: any) {
            console.log(e);
            this.messageService.add({
                severity: "error",
                summary: "Ошибка...",
                detail: e.error.message,
                life: 5000
            });
        }
        this.submit.emit($event);
        this.visible = false;
    }

    getSaveLabel() {
        return this.editMode ? "Обновить" : "Создать";
    }

    addEmployeeToList(employee: Employee) {
        if (!(this.selectedEmployees.find(emp => emp.id === employee.id))) {
            this.selectedEmployees.push(employee);
            this.validateChecker();
        }
    }

    deleteSkill(id: string) {
        this.selectedEmployees = this.selectedEmployees.filter(e => e.id !== id);
    }

    validateChecker() {
        this.inviteService.validateInterview(this.interview);
    }
}
