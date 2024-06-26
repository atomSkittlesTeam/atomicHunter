import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Competence} from "../../dto/Competence";
import {VacancyService} from "../../services/vacancy.service";
import {CompetenceService} from "../../services/competence.service";
import {PositionService} from "../../services/position.service";
import {MessageService} from "primeng/api";
import {CompetenceGroupDto} from "../../dto/CompetenceGroupDto";

@Component({
  selector: 'app-competence-group-dialog',
  templateUrl: './competence-group-dialog.component.html',
  styleUrls: ['./competence-group-dialog.component.scss']
})
export class CompetenceGroupDialogComponent {
  @Input("item") get item(): CompetenceGroupDto {
    return this._item;
  }

  set item(value: CompetenceGroupDto) {
    if (value) {
      this._item = value;
    } else {
      this._item = new CompetenceGroupDto();
    }
  }

  @Input("openDialog") visible: boolean = false;
  private _item: CompetenceGroupDto;
  @Input("editMode") editMode: boolean;
  @Output() submit = new EventEmitter<any>();
  @Output() visibleChange = new EventEmitter<any>();
  dialogTitle = "Заведение вакансии";
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
      this.dialogTitle = "Редактирование группы";
    } else {
      this.dialogTitle = "Регистрация группы";
    }
    this.loading = false;
  }

  async onSubmit($event?: any) {
    if ($event !== null) { // null передается, если закрыть форму без сохранения на крестик
      if (this.editMode) {
        await this.updateCompetenceGroup(this._item);
      } else {
        await this.createCompetenceGroup(this._item);
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

  async createCompetenceGroup(competenceGroup: CompetenceGroupDto) {
    try {
      this.loading = true;
      const rq = await this.competenceService.createCompetenceGroup(competenceGroup);
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

  async updateCompetenceGroup(competenceGroup: CompetenceGroupDto) {
    try {
      this.loading = true;
      const rq = await this.competenceService.updateCompetenceGroup(competenceGroup.id, competenceGroup);
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
