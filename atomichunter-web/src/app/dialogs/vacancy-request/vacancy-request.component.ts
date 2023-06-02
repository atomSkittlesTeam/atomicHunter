import { Component, EventEmitter, Input, Output } from "@angular/core";
import { Vacancy } from "../../dto/Vacancy";
import { VacancyRequest } from "../../dto/VacancyRequest";
import { InviteService } from "../../services/invite.service";
import { MessageService } from "primeng/api";
import { VacancyRespond } from "../../dto/VacancyRespond";

@Component({
  selector: "app-vacancy-request",
  templateUrl: "./vacancy-request.component.html",
  styleUrls: ["./vacancy-request.component.scss"]
})
export class VacancyRequestComponent {

  @Input("openDialog") visible: boolean = false;
  @Input("item") item: VacancyRequest = new VacancyRequest();
  @Input("selectedVacancyRespond") selectedVacancyRespond: VacancyRespond = new VacancyRespond();

  @Input("editMode") editMode: boolean;
  @Output() submit = new EventEmitter<any>();
  @Output() visibleChange = new EventEmitter<any>();
  dialogTitle = "Заведение собрания";

  constructor(private inviteService: InviteService,
              private messageService: MessageService) {
  }


  closeDialog($event?: any) {
    this.submit.emit($event);
    this.visible = false;
  }

  async onSubmit($event?: any) {
    console.log(this.item);

    try {
      await this.inviteService.inviteToInterview(this.selectedVacancyRespond);
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
