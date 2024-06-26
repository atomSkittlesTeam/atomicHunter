import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Vacancy} from "../../dto/Vacancy";
import {Position} from "../../dto/Position";
import {Competence} from "../../dto/Competence";
import {VacancyService} from "../../services/vacancy.service";
import {CompetenceService} from "../../services/competence.service";
import {PositionService} from "../../services/position.service";
import {MessageService} from "primeng/api";
import {CompetenceWeight} from "../../dto/CompetenceWeight";
import {VacancyRespond} from "../../dto/VacancyRespond";

@Component({
  selector: 'app-vacancy-respond-dialog',
  templateUrl: './vacancy-respond-dialog.component.html',
  styleUrls: ['./vacancy-respond-dialog.component.scss']
})
export class VacancyRespondDialogComponent {
  @Input("item") get item(): VacancyRespond {
    return this._item;
  }

  set item(value: VacancyRespond) {
    if (value) {
      this._item = value;
    } else {
      this._item = new VacancyRespond();
    }
  }

  @Input("vacancy") get vacancy(): Vacancy {
    return this._vacancy;
  }

  set vacancy(value: Vacancy) {
    if (value) {
      this._vacancy = value;
    } else {
      this._vacancy = new Vacancy();
    }
  }

  @Input("openDialog") visible: boolean = false;
  private _item: VacancyRespond;
  private _vacancy: Vacancy;
  @Input("editMode") editMode: boolean;
  @Output() submit = new EventEmitter<any>();
  @Output() visibleChange = new EventEmitter<any>();
  dialogTitle = "Заведение вакансии";
  positions: Position[] = [];
  value: any;

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
      this._item = await this.vacancyService.getVacancyRespondById(this._item.id);
      this.dialogTitle = "Редактирование вакансии";
    } else {
      this._item = new VacancyRespond();
      this._item.vacancyId = this._vacancy.id;
      this.dialogTitle = "Регистрация кандидата";
    }
    this.loading = false;
  }

  async onSubmit($event?: any) {
    if ($event !== null) { // null передается, если закрыть форму без сохранения на крестик
      if (this.editMode) {
        await this.updateVacancyRespond(this._item);
      } else {
        await this.createVacancyRespond(this._item);
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

  async createVacancyRespond(vacancyRespond: VacancyRespond) {
    try {
      this.loading = true;
      const rq = await this.vacancyService.createVacancyRespond(vacancyRespond);
      this.messageService.add({
        severity: "success",
        summary: "Успех!",
        detail: "Добавлен кандидат на вакансию",
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

  async updateVacancyRespond(vacancyRespond: VacancyRespond) {
    try {
      this.loading = true;
      const rq = await this.vacancyService.updateVacancyRespond(vacancyRespond.id, vacancyRespond);
      this.messageService.add({
        severity: "success",
        summary: "Успех!",
        detail: "Информация о кандидате обновлена",
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
}
