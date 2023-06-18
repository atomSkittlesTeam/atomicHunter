import {Component, EventEmitter, Input, Output} from "@angular/core";
import {Interview} from "../../dto/Interview";
import {InterviewService} from "../../services/interview.service";
import {MessageService} from "primeng/api";
import {VacancyRespond} from "../../dto/VacancyRespond";
import {Employee} from "../../dto/Employee";
import {OrgStructService} from "../../services/org-struct.service";

@Component({
  selector: "app-interview-dialog",
  templateUrl: "./interview-dialog.component.html",
  styleUrls: ["./interview-dialog.component.scss"]
})
export class VacancyRequestComponent {

  @Input("openDialog") visible: boolean = false;
  @Input("item") interview: Interview = new Interview();
  @Input("selectedVacancyRespond") selectedVacancyRespond: VacancyRespond = new VacancyRespond();

  @Input("editMode") editMode: boolean;
  @Output() submit = new EventEmitter<any>();
  @Output() visibleChange = new EventEmitter<any>();
  dialogTitle = "Организация собеседования";
  showSidebarWithAllSkills: boolean = false;
  employees: Employee[] = [];
  selectedEmployees: Employee[] = [];


  constructor(private inviteService: InterviewService,
              private orgStructService: OrgStructService,
              private messageService: MessageService) {
  }

  async ngOnInit() {
    if (this.selectedVacancyRespond.interviewId) {
      this.interview = await this.inviteService.getInterviewById(this.selectedVacancyRespond.interviewId);
      if (typeof this.interview.dateEnd === "number") {
        this.interview.dateEnd = new Date(this.interview.dateEnd * 1000);
      }
      // new Date(data.value * 1000).toLocaleDateString()
      // + ' ' + new Date(data.value * 1000).toLocaleTimeString() : '';

      this.dialogTitle = "Редактирование собеседования";
    }
}

  async openSidebarWithAllSkills() {
    this.showSidebarWithAllSkills = true;
    this.employees = await this.orgStructService.getEmployees();
    // this.competencesAll = await this.competenceService.getAllCompetences();

  }

  closeDialog($event?: any) {
    this.submit.emit($event);
    this.visible = false;
  }

  async onSubmit($event?: any) {
    try {
      if (this.editMode) {
        await this.inviteService.updateInterview(this.selectedVacancyRespond.id, this.interview);
        this.messageService.add({
          severity: "success",
          summary: "Успех!",
          detail: "Собеседование обновлено!",
          life: 5000
        });
      } else {
        await this.inviteService.createInterview(this.selectedVacancyRespond.id, this.interview);
        this.messageService.add({
          severity: "success",
          summary: "Успех!",
          detail: "Собеседование создано!",
          life: 5000
        });
      }

    } catch (e: any) {
      console.log(e);
      this.messageService.add({
        severity: "error",
        summary: "Ошибка...",
        detail: e.error.message,
        life: 5000
      });
    }
    this.submit.emit($event);
    this.visible = false;
  }

  getSaveLabel() {
    return this.editMode ? "Обновить" : "Создать";
  }

  addEmployeeToList(employee: Employee) {
    if (!(this.selectedEmployees.find(emp => emp.id === employee.id))) {
      this.selectedEmployees.push(employee);
    }
  }

  deleteSkill(id: string) {
    this.selectedEmployees = this.selectedEmployees.filter(e => e.id !== id);
  }
}
