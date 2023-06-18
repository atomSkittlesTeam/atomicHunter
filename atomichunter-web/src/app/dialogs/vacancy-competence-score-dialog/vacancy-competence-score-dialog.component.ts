import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Vacancy} from "../../dto/Vacancy";
import {StaffUnitDto} from "../../dto/StaffUnitDto";
import {Position} from "../../dto/Position";
import {Employee} from "../../dto/Employee";
import {Competence} from "../../dto/Competence";
import {CompetenceGroupsWithCompetencesDto} from "../../dto/CompetenceGroupsWithCompetencesDto";
import {VacancyService} from "../../services/vacancy.service";
import {CompetenceService} from "../../services/competence.service";
import {OrgStructService} from "../../services/org-struct.service";
import {PositionService} from "../../services/position.service";
import {MessageService} from "primeng/api";
import {CompetenceWeight} from "../../dto/CompetenceWeight";
import {CompetenceWeightScore} from "../../dto/CompetenceWeightScore";
import {VacancyCompetenceScoreRequestDto} from "../../dto/VacancyCompetenceScoreRequestDto";

@Component({
    selector: 'app-vacancy-competence-score-dialog',
    templateUrl: './vacancy-competence-score-dialog.component.html',
    styleUrls: ['./vacancy-competence-score-dialog.component.scss']
})
export class VacancyCompetenceScoreDialogComponent {
    @Input("item") get item(): Vacancy {
        return this._item;
    }

    set item(value: Vacancy) {
        if (value) {
            this._item = value;
        } else {
            this._item = new Vacancy();
        }
    }

    @Input("staffUnit") get staffUnit(): StaffUnitDto {
        return this._staffUnit;
    }

    set staffUnit(value: StaffUnitDto) {
        console.log(value, 'ssfafw')
        if (value) {
            this._staffUnit = value;
        } else {
            this._staffUnit = new StaffUnitDto();
        }
    }

    @Input("openDialog") visible: boolean = false;
    private _item: Vacancy;
    private _staffUnit: StaffUnitDto;
    @Input("editMode") editMode: boolean;
    @Input("singlePosition") singlePosition: Position;
    @Output() submit = new EventEmitter<any>();
    @Output() visibleChange = new EventEmitter<any>();
    dialogTitle = "Заведение вакансии";
    positions: Position[] = [];
    employees: Employee[] = [];
    vacancyCompetenceScoreRequest: VacancyCompetenceScoreRequestDto = new VacancyCompetenceScoreRequestDto();
    value: any;

    competenceWeightMaxSum = 100; //todo МБ статиком куда-нибудь вынести?

    get sum(): number {
        return this.weightSum();
    }


    competences: Competence[] = [];
    competencesAll: Competence[] = [];
    competenceWeightScores: CompetenceWeightScore[] = [];
    competenceGroupsWithCompetences: CompetenceGroupsWithCompetencesDto[] = [];
    loading: boolean = false;
    showSidebarWithAllSkills: boolean = false;

    constructor(private vacancyService: VacancyService,
                private competenceService: CompetenceService,
                private orgStructService: OrgStructService,
                private positionService: PositionService,
                public messageService: MessageService) {
    }

    ngOnInit() {
        this.init();
    }

    async init() {
        this.loading = true;
        await this.getAllPositionsFromApi();
        await this.getEmployeeFromApi();
        this.competenceWeightScores = await this.competenceService.getCompetencesWeightScoreById(this.item.id);
        console.log(this.competenceWeightScores);
        this.competenceGroupsWithCompetences = await this.competenceService.getAllCompetenceTree();
        if (this.editMode) {
            this._item = await this.vacancyService.getVacancyById(this._item.id);
            this.dialogTitle = "Редактирование вакансии";
        } else {
            this.dialogTitle = "Регистрация вакансии";
        }
        this.loading = false;
    }

    async onSubmit($event?: any) {

        if (this.staffUnit) {
            this.item.staffUnit = this.staffUnit;
            this.item.position = this.singlePosition;
        }
        if ($event !== null) { // null передается, если закрыть форму без сохранения на крестик
            this.vacancyCompetenceScoreRequest.vacancyRespondId = this.item.id;
            this.vacancyCompetenceScoreRequest.employee = this.staffUnit.employee;
            this.vacancyCompetenceScoreRequest.competenceWeightScoreList = this.competenceWeightScores;
            this.vacancyCompetenceScoreRequest.interviewId = 1;
            if (this.editMode) {
                await this.updateVacancy(this._item);
            } else {
                await this.createVacancy(this._item);
            }
        }
        this.submit.emit($event);
        this.visible = false;
    }

    closeDialog($event?: any) {
        this.submit.emit($event);
        this.visible = false;
    }

    getSaveLabel() {
        return this.editMode ? "Обновить" : "Создать";
    }

    pushNewSkill(id: number, indexGroup?: number) {
        let pickedSkill: any = this.competencesAll.find(com => com.id === id);
        if (this._item.competenceWeight == null) {
            this._item.competenceWeight = [];
        }
        if (!(this.competences.find(com => com.id === pickedSkill.id))) {
            this.competences.push(pickedSkill);
        }
        this.competences.map(comp => {
            if (!this._item.competenceWeight.map(e => e.competence.id).includes(comp.id)) {
                this._item.competenceWeight.push(new CompetenceWeight(comp, 10));
            }
        });
    }

    async createVacancy(vacancy: Vacancy) {
        try {
            this.loading = true;
            console.log(this.vacancyCompetenceScoreRequest);
            // const rq = await this.vacancyService.createVacancy(vacancy);
            this.messageService.add({
                severity: "success",
                summary: "Успех!",
                detail: "Вакания создана",
                life: 5000
            });
        } catch (e: any) {
            this.messageService.add({
                severity: "error",
                summary: "Ошибка...",
                detail: e.error.message,
                life: 5000
            });
        } finally {
            this.loading = false;
        }
    }

    async updateVacancy(vacancy: Vacancy) {
        try {
            this.loading = true;
            const rq = await this.vacancyService.updateVacancy(vacancy.id, vacancy);
            this.messageService.add({
                severity: "success",
                summary: "Успех!",
                detail: "Вакансия обновлена",
                life: 5000
            });
        } catch (e: any) {
            this.messageService.add({
                severity: "error",
                summary: "Ошибка...",
                detail: e.error.message,
                life: 5000
            });
        } finally {
            this.loading = false;
        }
    }

    async getAllPositionsFromApi() {
        let positions = await this.positionService.getPositions();
        this.positions = positions;
    }

    async getEmployeeFromApi() {
        this.employees = await this.orgStructService.getHrEmployees();
    }


    weightSum() {
        let sum = 0;
        if (!this._item?.competenceWeight?.length) {
            return sum;
        }
        this._item.competenceWeight.forEach(competence => sum += competence.weight);
        return sum;
    }
}
