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
import {VacancyRespond} from "../../dto/VacancyRespond";

@Component({
    selector: 'app-vacancy-competence-score-dialog',
    templateUrl: './vacancy-competence-score-dialog.component.html',
    styleUrls: ['./vacancy-competence-score-dialog.component.scss']
})
export class VacancyCompetenceScoreDialogComponent {
    blockCreate: boolean = true;

    @Input("itemRespond") get itemRespond(): VacancyRespond {
        return this._itemRespond;
    }

    set itemRespond(value: VacancyRespond) {
        if (value) {
            this._itemRespond = value;
        } else {
            this._itemRespond = new VacancyRespond();
        }
    }

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
    private _itemRespond: VacancyRespond;
    private _staffUnit: StaffUnitDto;
    @Input("editMode") editMode: boolean;
    @Input("singlePosition") singlePosition: Position;
    @Input("employeeExpert") employeeExpert: Employee;
    @Input("competenceWeightScoreForExpert") competenceWeightScoreForExpert: CompetenceWeightScore[] = [];
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
    binaryLogicCheckbox: boolean = false;

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
        console.log(this.employeeExpert,'employeeExpert')
        console.log(this.staffUnit,'staffUnit')
        let competenceWeightScoreForExpert: CompetenceWeightScore[] = this.competenceWeightScoreForExpert;
        if (competenceWeightScoreForExpert.length === 0) {
            console.log(this.competenceWeightScores,'11111111')

            this.competenceWeightScores = await this.competenceService.getCompetencesWeightScoreById(this.itemRespond.id);
        } else {
            console.log(this.competenceWeightScores,'22222222')
            this.competenceWeightScores = this.competenceWeightScoreForExpert;
            this.staffUnit.employee = this.employeeExpert;
            console.log(this.competenceWeightScoreForExpert);
        }
        this.competenceWeightScores.forEach(e => e.score = 1);
        this.competenceGroupsWithCompetences = await this.competenceService.getAllCompetenceTree();
        console.log(this.competenceWeightScores,'this.competenceWeightScores.')
        if (this.editMode) {
            this._item = await this.vacancyService.getVacancyById(this._item.id);
            this.dialogTitle = "Оценка экспертом";
        } else {
            this.dialogTitle = "Просмотр Эксперта";
        }
        this.loading = false;
    }

    async onSubmit($event?: any) {
        this.competenceWeightScores.forEach(dto => {
            if (dto.competence.binaryLogic) {
                dto.score = dto.binaryIsChecked ? 10 : 0;
            }
        });

        if (this.staffUnit) {
            this.item.staffUnit = this.staffUnit;
            this.item.position = this.singlePosition;
        }
        if ($event !== null) { // null передается, если закрыть форму без сохранения на крестик
            this.vacancyCompetenceScoreRequest.vacancyRespondId = this.itemRespond.id;
            this.vacancyCompetenceScoreRequest.employee = this.staffUnit.employee;
            this.vacancyCompetenceScoreRequest.competenceWeightScoreList = this.competenceWeightScores;
            // this.vacancyCompetenceScoreRequest.vacancyCompetenceId = this.competenceWeightScores
            this.vacancyCompetenceScoreRequest.interviewId = 1;

            await this.createVacancyCompetenceScore(this._item);
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
                this._item.competenceWeight.push(new CompetenceWeight(comp, 10, false));
            }
        });
    }

    pushGroupSkills(competences: any[]) {
        competences.forEach(ps => {
            this.pushNewSkill(ps.id)
        });
    }

    async createVacancyCompetenceScore(vacancy: Vacancy) {
        try {
            this.loading = true;
            const rq = await this.vacancyService.validateVacancyCompetenceScore(this.vacancyCompetenceScoreRequest);
            this.messageService.add({
                severity: "success",
                summary: "Успех!",
                detail: "Оценка сохранена",
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

    async updateVacancyCompetenceScore(vacancy: Vacancy) {
        try {
            this.loading = true;
            const rq = await this.vacancyService.updateVacancy(vacancy.id, vacancy);
            this.messageService.add({
                severity: "success",
                summary: "Успех!",
                detail: "Оценка обновлена",
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
        this.employees = await this.orgStructService.getEmployees();
    }


    weightSum() {
        let sum = 0;
        if (!this._item?.competenceWeight?.length) {
            return sum;
        }
        this._item.competenceWeight.forEach(competence => sum += competence.weight);
        return sum;
    }

    calculateBinary(score: number) {
        console.log()
        this.binaryLogicCheckbox ? score = 10 : score = 0;
    }

    async validateExpertScore() {
        if (this.competenceWeightScoreForExpert.length > 0) {
            this.blockCreate = true;
        } else if (this.competenceWeightScoreForExpert.length === 0 && this.editMode == true) {
            this.blockCreate = true;
        } else {
            this.blockCreate = await this.vacancyService.validateVacancyCompetenceScoreByEmployeeId(this.staffUnit.employee?.id, this.itemRespond.id);
        }
    }
}
