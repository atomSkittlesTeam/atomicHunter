import {Component, EventEmitter, Input, Output} from "@angular/core";
import {MessageService} from "primeng/api";
import {Position} from "../../dto/Position";
import {Vacancy} from "../../dto/Vacancy";
import {PositionService} from "../../services/position.service";
import {VacancyService} from "../../services/vacancy.service";
import {Competence} from "../../dto/Competence";
import {CompetenceService} from "../../services/competence.service";
import {CompetenceWeight} from "../../dto/CompetenceWeight";
import {StaffUnitDto} from "../../dto/StaffUnitDto";
import {Employee} from "../../dto/Employee";
import {OrgStructService} from "../../services/org-struct.service";
import {CompetenceGroupsWithCompetencesDto} from "../../dto/CompetenceGroupsWithCompetencesDto";
import {CompetenceWeightScore} from "../../dto/CompetenceWeightScore";

@Component({
    selector: "app-vacancy-dialog",
    templateUrl: "./vacancy-dialog.component.html",
    styleUrls: ["./vacancy-dialog.component.scss"]
})
export class VacancyDialogComponent {

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
        console.log(value,'ssfafw')
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
    // @Input("staffUnit") staffUnit: StaffUnitDto = new StaffUnitDto();
    @Input("singlePosition") singlePosition: Position;
    @Output() submit = new EventEmitter<any>();
    @Output() visibleChange = new EventEmitter<any>();
    dialogTitle = "Заведение вакансии";
    positions: Position[] = [];
    employees: Employee[] = [];
    value: any;

    competenceWeightMaxSum = 100; //todo МБ статиком куда-нибудь вынести?

    get sum(): number {
        return this.weightSum();
    }


    competences: Competence[] = [];
    competencesAll: Competence[] = [];
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
        this.competenceGroupsWithCompetences = await this.competenceService.getAllCompetenceTree();
        if (this.editMode) {
            this._item = await this.vacancyService.getVacancyById(this._item.id);
            this.dialogTitle = "Редактирование вакансии";
        } else {
            this.dialogTitle = "Регистрация вакансии";
        }
        this.loading = false;
    }

    async selectPosition() {
        this.competences = [];
        this._item.competenceWeight = [];
        // this.competences = await this.competenceService.getCompetencesByPositionId(Number.parseInt(this._item?.position?.id));
        this.competences.map(comp => this._item.competenceWeight.push(new CompetenceWeight(comp, 10, false)));
    }

    async selectEmployee() {

    }

    async onSubmit($event?: any) {
        if (this.staffUnit) {
            this.item.staffUnit = this.staffUnit;
            this.item.position = this.singlePosition;
        }
        if ($event !== null) { // null передается, если закрыть форму без сохранения на крестик
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

    async openSidebarWithAllSkills() {
        this.showSidebarWithAllSkills = true;
        this.competencesAll = await this.competenceService.getAllCompetences();
        // let alreadyHas: number[] = [];
        // console.log(this.competencesAll, "fwafawf");
        //
        // this.competencesAll = this.competencesAll.map((e, idx) => {
        //   if (this.competences.find(com => com.name = e.name)) {
        //     alreadyHas.push(idx);
        //   }
        //   return e;
        // });
        // alreadyHas.forEach(index => this.competencesAll.splice(index, 1));
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

    async createVacancy(vacancy: Vacancy) {
        try {
            this.loading = true;
            const rq = await this.vacancyService.createVacancy(vacancy);
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

    isInvalidSumWeight(): boolean {
        return this.sum != this.competenceWeightMaxSum;
    }

    deleteSkill(id: number) {
        this._item.competenceWeight = this._item.competenceWeight
            .filter(e => e.competence.id !== id);

        this.competences = this.competences.filter(e => e.id !== id);
    }

    setCompetenceWeightSumToMax() {
        let normalizationCoefficient = (this.competenceWeightMaxSum / this.sum); //на него умножаем каждый вес
        this._item.competenceWeight.map(e => {
            e.weight = Math.round(e.weight * normalizationCoefficient);
        });
        if (this.sum > this.competenceWeightMaxSum) {
            this.roundingWeightsToMaxSum(this.sum - this.competenceWeightMaxSum, true);
        } else if (this.sum < this.competenceWeightMaxSum) {
            this.roundingWeightsToMaxSum(this.competenceWeightMaxSum - this.sum, false);
        }
    }

    roundingWeightsToMaxSum(differenceCurrentAndMax: number, isHigher: boolean) {
        for (let i = 0; i < differenceCurrentAndMax; i++) {
            this._item.competenceWeight[i].weight += isHigher ? -1 : 1;
        }
    }
}
