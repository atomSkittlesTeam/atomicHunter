import { Component, EventEmitter, Input, Output } from "@angular/core";
import { Vacancy } from "../../dto/Vacancy";
import { Interview } from "../../dto/Interview";
import { InviteService } from "../../services/invite.service";
import { MessageService } from "primeng/api";
import { VacancyRespond } from "../../dto/VacancyRespond";

@Component({
  selector: "app-interview-dialog",
  templateUrl: "./interview-dialog.component.html",
  styleUrls: ["./interview-dialog.component.scss"]
})
export class VacancyRequestComponent {

  @Input("openDialog") visible: boolean = false;
  @Input("item") item: Interview = new Interview();
  @Input("selectedVacancyRespond") selectedVacancyRespond: VacancyRespond = new VacancyRespond();

  @Input("editMode") editMode: boolean;
  @Output() submit = new EventEmitter<any>();
  @Output() visibleChange = new EventEmitter<any>();
  dialogTitle = "Приглашение на собеседование";
  interview: Interview = new Interview();

  constructor(private inviteService: InviteService,
              private messageService: MessageService) {
  }

  async ngOnInit() {
    if (this.selectedVacancyRespond.interviewId) {  
      this.interview = await this.inviteService.getInterviewById(this.selectedVacancyRespond.interviewId);
      this.dialogTitle = "Редактирование приглашения";
    }
}

  closeDialog($event?: any) {
    this.submit.emit($event);
    this.visible = false;
  }

  async onSubmit($event?: any) {
    try {
      await this.inviteService.inviteToInterview(this.selectedVacancyRespond.id, this.interview);
      this.messageService.add({
        severity: "success",
        summary: "Успех!",
        detail: "Приглашение на собеседование отправлено!"
      });
    } catch (e: any) {
      this.messageService.add({
        severity: "error",
        summary: "Ошибка...",
        detail: e.error.message
      });
    }
    this.submit.emit($event);
    this.visible = false;
  }

  getSaveLabel() {
    return this.editMode ? "Обновить" : "Создать";
  }
}