import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Competence} from "../../dto/Competence";
import {VacancyService} from "../../services/vacancy.service";
import {CompetenceService} from "../../services/competence.service";
import {PositionService} from "../../services/position.service";
import {MessageService} from "primeng/api";
import {CompetenceGroupDto} from "../../dto/CompetenceGroupDto";

@Component({
    selector: 'app-competence-dialog',
    templateUrl: './competence-dialog.component.html',
    styleUrls: ['./competence-dialog.component.scss']
})
export class CompetenceDialogComponent {
    @Input("item") get item(): Competence {
        return this._item;
    }

    set item(value: Competence) {
        if (value) {
            this._item = value;
        } else {
            this._item = new Competence();
        }
    }

    @Input("competenceGroup") get competenceGroup(): CompetenceGroupDto {
        return this._competenceGroup;
    }

    set competenceGroup(value: CompetenceGroupDto) {
        if (value) {
            this._competenceGroup = value;
        } else {
            this._competenceGroup = new CompetenceGroupDto();
        }
    }

    @Input("openDialog") visible: boolean = false;
    private _item: Competence;
    private _competenceGroup: CompetenceGroupDto;
    @Input("editMode") editMode: boolean;
    @Output() submit = new EventEmitter<any>();
    @Output() visibleChange = new EventEmitter<any>();
    dialogTitle = "Заведение навыка";
    value: any;


    competences: Competence[] = [];
    loading: boolean = false;

    constructor(private vacancyService: VacancyService,
                private competenceService: CompetenceService,
                private positionService: PositionService,
                public messageService: MessageService) {
    }

    ngOnInit() {
        this.init();
    }

    async init() {
        this.loading = true;
        if (this.editMode) {
            // this._item = await this.vacancyService.getVacancyById(this._item.id);
            this.dialogTitle = "Редактирование навыка";
        } else {
            this.dialogTitle = "Регистрация навыка";
        }
        this.loading = false;
    }

    async onSubmit($event?: any) {
        if ($event !== null) { // null передается, если закрыть форму без сохранения на крестик
            if (this.editMode) {
                await this.updateCompetence(this._item);
            } else {
                await this.createCompetence(this._item);
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

    async createCompetence(competence: Competence) {
        try {
            console.log(this.competenceGroup, "fafawfawf")
            this.loading = true;
            const rq = await this.competenceService.createCompetence(this.competenceGroup.id, competence);
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

    async updateCompetence(competence: Competence) {
        try {
            this.loading = true;
            const rq = await this.competenceService.updateCompetence(competence.id, competence);
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

}
