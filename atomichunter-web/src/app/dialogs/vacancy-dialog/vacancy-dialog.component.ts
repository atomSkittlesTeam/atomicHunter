import { Component, EventEmitter, Input, Output } from "@angular/core";
import { MessageService } from "primeng/api";
import { Position } from "../../dto/Position";
import { Vacancy } from "../../dto/Vacancy";
import { PositionService } from "../../services/position.service";
import { VacancyService } from "../../services/vacancy.service";
import { Competence } from "../../dto/Competence";
import { CompetenceService } from "../../services/competence.service";
import { CompetenceWeight } from "../../dto/CompetenceWeight";

@Component({
  selector: "app-vacancy-dialog",
  templateUrl: "./vacancy-dialog.component.html",
  styleUrls: ["./vacancy-dialog.component.scss"]
})
export class VacancyDialogComponent {

  @Input("openDialog") visible: boolean = false;
  @Input("item") item: Vacancy;
  @Input("editMode") editMode: boolean;
  @Output() submit = new EventEmitter<any>();
  @Output() visibleChange = new EventEmitter<any>();
  dialogTitle = "Заведение вакансии";
  positions: Position[] = [];
  buttons: any[] = [];
  value: any;
  weight: number = 0;
  summ = 0;

  competences: Competence[] = [];
  loading: boolean = false;


  constructor(private vacancyService: VacancyService,
              private competenceService: CompetenceService,
              private positionService: PositionService,
              public messageService: MessageService) {
  }

  async ngOnInit() {
    this.loading = true;
    await this.getAllPositionsFromApi();
    if (this.editMode) {
      this.item = await this.vacancyService.getVacancyById(this.item.id);
      this.dialogTitle = "Редактирование вакансии";
    } else {
      this.item = new Vacancy();
      this.item.position = new Position();
      this.dialogTitle = "Регистрация вакансии";
    }
    this.loading = false;
    console.log(this.item);
  }

  async selectPosition() {
    this.competences = [];
    this.item.competenceWeight = [];
    this.competences = await this.competenceService.getCompetencesByPositionId(this.item?.position?.id);
    this.competences.map(comp => this.item.competenceWeight.push(new CompetenceWeight(comp, 10)));
  }

  async onSubmit($event?: any) {
    if ($event !== null) { // null передается, если закрыть форму без сохранения на крестик
      if (this.editMode) {
        await this.updateVacancy(this.item);
      } else {
        await this.createVacancy(this.item);
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

  async createVacancy(vacancy: Vacancy) {
    try {
      const rq = await this.vacancyService.createVacancy(vacancy);
      this.messageService.add({
        severity: "success",
        summary: "Успех!",
        detail: "Вакания создана"
      });
    } catch (e: any) {
      this.messageService.add({
        severity: "error",
        summary: "Ошибка...",
        detail: e.error.message
      });
    }
  }

  async updateVacancy(vacancy: Vacancy) {
    try {
      const rq = await this.vacancyService.updateVacancy(vacancy.id, vacancy);
      this.messageService.add({
        severity: "success",
        summary: "Успех!",
        detail: "Вакансия обновлена"
      });
    } catch (e: any) {
      this.messageService.add({
        severity: "error",
        summary: "Ошибка...",
        detail: e.error.message
      });
    }
  }

  async getAllPositionsFromApi() {
    this.positions = await this.positionService.getPositions();
  }


  weightSum() {
    this.summ = 0;
    this.item.competenceWeight.forEach(competence => this.summ += competence.weight);
    return this.summ;
  }

  deleteSkill(id: number) {
    this.item.competenceWeight = this.item.competenceWeight
      .filter(e => e.competence.id !== id);
  }


}
