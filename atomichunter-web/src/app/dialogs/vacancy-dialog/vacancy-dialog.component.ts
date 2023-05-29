import { Component, EventEmitter, Input, Output } from "@angular/core";
import { MessageService } from "primeng/api";
import { Position } from "src/app/dto/Position";
import { Vacancy } from "src/app/dto/Vacancy";
import { PositionService } from "src/app/services/position.service";
import { VacancyService } from "src/app/services/vacancy.service";
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
  positions: any[] = [];
  buttons: any[] = [];
  value: any;
  weight: number = 0;
  summ = 0;

  competences: Competence[] = [];


  constructor(private vacancyService: VacancyService,
              private competenceService: CompetenceService,
              private positionService: PositionService,
              public messageService: MessageService) {

  }

  async ngOnInit() {
    await this.getAllPositionsFromApi();
    if (this.editMode) {
      this.dialogTitle = "Редактирование вакансии";
    } else {
      this.item = new Vacancy();
      this.item.position = new Position();
      this.dialogTitle = "Регистрация вакансии";
    }
    this.competences = await this.competenceService.getCompetencesByPositionId(1);
    this.competences.forEach(comp => this.item.competenceWeight.push(new CompetenceWeight(comp, 10)));
    console.log(this.item);
  }

  async onSubmit($event: any) {
    if ($event !== null) { // null передается, если закрыть форму без сохранения на крестик
      if (this.editMode) {
        await this.updateVacancy($event);
      } else {
        await this.createVacancy($event);
      }
    }
    this.submit.emit($event);
    this.visible = false;
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
