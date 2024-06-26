import {Component, EventEmitter, Input, Output, ViewChild} from "@angular/core";
import {Vacancy} from "../../dto/Vacancy";
import {VacancyRespond} from "../../dto/VacancyRespond";
import {CellClickedEvent, ColDef, GridReadyEvent} from "ag-grid-community";
import {AgGridAngular} from "ag-grid-angular";
import {ConfirmationService, MenuItem, MessageService} from "primeng/api";
import {LoadingCellRendererComponent} from "../../platform/loading-cell-renderer/loading-cell-renderer.component";
import {VacancyService} from "../../services/vacancy.service";
import {InterviewService} from "src/app/services/interview.service";
import {VacancyWithVacancyRespond} from "../../dto/VacancyWithVacancyRespond";
import {StaffUnitDto} from "../../dto/StaffUnitDto";
import {Employee} from "../../dto/Employee";
import {OrgStructService} from "../../services/org-struct.service";
import {CompetenceService} from "../../services/competence.service";
import {CompetenceWeightScore} from "../../dto/CompetenceWeightScore";
import {OfferEnum} from "../../dto/offer-enum";
import {OfferService} from "../../services/offer.service";

@Component({
  selector: "app-vacancy-respond",
  templateUrl: "./vacancy-respond.component.html",
  styleUrls: ["./vacancy-respond.component.scss"]
})
export class VacancyRespondComponent {
  meetingItems: any = [];
  offerItems: any = [];
  staffUnit: StaffUnitDto = new StaffUnitDto();
  items: MenuItem[];
  openInterviewDialog: boolean = false;
  openVacancyRespondDialog: boolean = false;
  interviewDialogEditMode: boolean = false;
  vacancyRespondDialogEditMode: boolean = false;
  openDialogVacancyComp: boolean = false;
  dialogEditMode: boolean = false;
  offerType: OfferEnum;


  constructor(private confirmationService: ConfirmationService,
              private messageService: MessageService,
              private competenceService: CompetenceService,
              private orgStructService: OrgStructService,
              private vacancyService: VacancyService,
              private inviteService: InterviewService,
              private offerService: OfferService,
              private interviewService: InterviewService) {
    this.renderMenu();
  }

  private _vacancy: Vacancy;
  filter: boolean = false;

  public get vacancy(): Vacancy {
    return this._vacancy;
  }

  @Input("vacancy")
  public set vacancy(value: Vacancy) {
    this._vacancy = value;
    this.getRespondByVacancyIdFromApi();
    this.selectedVacancyRespond = new VacancyRespond();
    this.renderMenu();
  }

  @Output("pdfResume") pdfResume = new EventEmitter<string>();

  selectedVacancyRespond: VacancyRespond;
  showArchive = false;
  public rowData: any[] = [];
  employees: Employee[] = [];
  selectedEmployee: Employee;
  competenceWeightScoreForExpert: CompetenceWeightScore[] = [];
  competenceWeightScoreForExpert2: CompetenceWeightScore[] = [];


  public overlayLoadingTemplate = "<div class=\"loading-text\"> Загрузка...</div> ";

  createVacancyRespond() {
    this.openVacancyRespondDialog = true;
    this.vacancyRespondDialogEditMode = false;
  }

  async getEmployeeFromApi() {
    this.employees = await this.competenceService.getEmployeesWithScoreForRespond(this.selectedVacancyRespond.id);
  }

  updateVacancyRespond() {
    this.openVacancyRespondDialog = true;
    this.vacancyRespondDialogEditMode = true;
  }

  renderMenu() {
    this.items = [
      {
        label: "Кандидат",
        items: [
          {
            label: "Добавить",
            disabled: !this._vacancy,
            icon: "pi pi-plus",
            command: () => {
              if (this._vacancy.id) {
                this.createVacancyRespond();
              }
            }
          },
          {
            label: "Редактировать",
            disabled: !this.selectedVacancyRespond || !this.selectedVacancyRespond.id,
            icon: "pi pi-pencil",
            command: () => {
              if (this.selectedVacancyRespond.id) {
                this.updateVacancyRespond();
              }
            }
          },
          {
            label: "Удалить",
            disabled: !this.selectedVacancyRespond || !this.selectedVacancyRespond.id,
            icon: "pi pi-trash",
            command: () => {
              if (this.selectedVacancyRespond.id) {
                this.archiveRequestPosition();
              }
            }
          },
          {
            label: "Оценить кандидата",
            visible: !!this.selectedVacancyRespond && !!this.selectedVacancyRespond?.interviewId,
            disabled: !this.selectedVacancyRespond || !this.selectedVacancyRespond.id,
            icon: "pi pi-star",
            command: () => {
              if (this.selectedVacancyRespond.id) {
                this.selectedEmployee = new Employee();
                this.competenceWeightScoreForExpert = [];
                this.openDialogVacancyComp = true;
              }
            }
          },
          {
            label: "Открыть оценки кандидата",
            icon: "pi pi-check",
            visible: !!this.selectedVacancyRespond && !!this.selectedVacancyRespond?.interviewId,
            disabled: !this.selectedVacancyRespond || !this.selectedVacancyRespond.id,
            command: () => {
              if (this.selectedVacancyRespond.id) {
                this.visible = true;
                this.getEmployeeFromApi();
                }
            }
          },
          {
            label: this.selectedVacancyRespond && this.selectedVacancyRespond.interviewId
                ? "Редактировать собеседование" : "Организовать собеседование",
            disabled: !this.selectedVacancyRespond || !this.selectedVacancyRespond.id,
            visible: !!this.selectedVacancyRespond && !!this.selectedVacancyRespond.id,
            icon: "pi pi-envelope",
            command: () => {
              if (this.selectedVacancyRespond.id) {
                this.createInterview();
              }
            }
          },
          {
            visible: (this.selectedVacancyRespond && !!this.selectedVacancyRespond.interviewId),
            label: "Удалить собеседование",
            disabled: !this.selectedVacancyRespond || !this.selectedVacancyRespond.id,
            icon: "pi pi-trash",
            command: () => {
              if (this.selectedVacancyRespond.id) {
                this.deleteInterview();
              }
            }
          }
        ]
      },
      {
        label: "Оффер",
        items: [
          {
            label: "Отправить оффер",
            disabled: !this.selectedVacancyRespond || !this.selectedVacancyRespond.id,
            icon: "pi pi-envelope",
            command: () => {
              if (this.selectedVacancyRespond.id) {
                this.setOfferType();
              }
            }
          }
        ]
      }
    ];
  }

  async deleteInterview() {
    try {
      await this.interviewService.deleteInterview(this.selectedVacancyRespond.interviewId);
    } catch (e) {
      this.messageService.add({
        severity: "error",
        summary: "Ошибка!",
        detail: "Ошибка удаления интервью",
        life: 5000
      });
    } finally {
      await this.getRespondByVacancyIdFromApi();
    }
  }

  async onInterviewDialogSubmit($event: any) {
    this.openInterviewDialog = false;
    if ($event) {
      await this.getRespondByVacancyIdFromApi();
    }
  }

  async onVacancyRespondDialogSubmit($event: any) {
    this.openVacancyRespondDialog = false;
    if ($event) {
      await this.getRespondByVacancyIdFromApi();
    }
  }

  public columnDefs: ColDef[] = [
    { field: "id", headerName: "Идентификатор", filter: "agTextColumnFilter" },
    { field: "vacancyId", headerName: "Номер вакансии", filter: "agTextColumnFilter" },
    { field: "coverLetter", headerName: "Сопроводительное письмо", filter: "agTextColumnFilter" },
    { field: "lastName", headerName: "Фамилия", filter: "agTextColumnFilter"},
    { field: "firstName", headerName: "Имя", filter: "agTextColumnFilter"},
    { field: "email", headerName: "Email", filter: "agTextColumnFilter" },
    { field: "averageScore", headerName: "Средняя оценка собеседования", filter: "agTextColumnFilter" },
    {
      field: "interviewId",
      headerName: "Приглашен на собеседование",
      cellRenderer: (params: { value: any; }) => {
        return `<input disabled="true" type="checkbox" ${params.value ? "checked" : ""} />`;
      }
    },
    {
      field: "archive", headerName: "Архив", hide: !this.showArchive, cellRenderer: (params: { value: any; }) => {
        return `<input disabled="true" type="checkbox" ${params.value ? "checked" : ""} />`;
      }
    }
  ];

  // DefaultColDef sets props common to all Columns
  public defaultColDef: ColDef = {
    editable: false,
    sortable: true,
    flex: 1,
    minWidth: 100,
    filter: true,
    resizable: true,
    floatingFilter: this.filter
  };

  @ViewChild(AgGridAngular) agGrid!: AgGridAngular;

  async getRespondByVacancyIdFromApi() {
    if (this.vacancy && this.vacancy.id) {
      this.agGrid.api.showLoadingOverlay();
      this.rowData = await this.vacancyService.getVacancyRespondsByIds([this.vacancy.id], this.showArchive);
    }
  }

  public loadingCellRenderer: any = LoadingCellRendererComponent;
  public loadingCellRendererParams: any = {
    loadingMessage: "Подождите еще немного..."
  };
  visible: boolean = false;

  async onGridReady(params: GridReadyEvent) {
    if (this.vacancy && this.vacancy.id) {
      await this.getRespondByVacancyIdFromApi();
    }
  }

  onCellClicked(e: CellClickedEvent): void {
    this.selectedVacancyRespond = e.data;
    this.renderMenu();
  }

  showFilter() {
    this.filter = !this.filter;
    const columnDefs = this.agGrid.api.getColumnDefs();
    columnDefs?.forEach((colDef: any, index: number) => {
      colDef.floatingFilter = this.filter;
    });
    if (columnDefs) {
      this.agGrid.api.setColumnDefs(columnDefs);
    }
    this.agGrid.api.refreshHeader();
  }

  openRespondPdfResumeDialog() {
    this.pdfResume.emit(this.selectedVacancyRespond.pathToResume);
  }

  openVacancyAnalysTable() {
    // this.
  }

  archiveRequestPosition() {
    this.confirmationService.confirm({
      key: "vacancy-respond-archive",
      message: "Отправить позицию в архив?",
      accept: async () => {
        try {
          await this.vacancyService.archiveVacancyRespond(this.selectedVacancyRespond.id);
          this.messageService.add({
            severity: "success",
            summary: "Успех!",
            detail: "Позиция переведена в архив",
            life: 5000
          });
          await this.getRespondByVacancyIdFromApi();
        } catch (e: any) {
          this.messageService.add({
            severity: "error",
            summary: "Ошибка...",
            detail: e.error.message,
            life: 5000
          });
        }
      },
      reject: () => {
        // can implement on cancel
      }
    });
  }

  async showArchivePressed() {
    if (this.agGrid) {
      this.agGrid.columnApi.setColumnVisible("archive", this.showArchive);
    }
    await this.getRespondByVacancyIdFromApi();
  }

  async createInterview() {
    this.openInterviewDialog = true;
    if (this.selectedVacancyRespond.interviewId) {
      this.interviewDialogEditMode = true;
    } else {
      this.interviewDialogEditMode = false;
    }
  }

  setOfferType() {
    this.confirmationService.confirm({
      key: "vacancy-offer",
      accept: async () => {
        if (!this.offerType) {
          return;
        }
        try {
          await this.sendOffer();
          this.messageService.add({
            severity: 'success',
            summary: 'Успех!',
            detail: 'Оффер успешно отправлен',
            life: 5000
          });
          // await this.getAllVacanciesFromApi();
        } catch (e: any) {
          this.messageService.add({
            severity: 'error',
            summary: 'Ошибка...',
            detail: e.error.message,
            life: 5000
          });
        }
      },
      reject: () => {
        // can implement on cancel
      }
    });
  }


  async sendOffer() {
    try {
      let vacancyWithVacancyRespond = new VacancyWithVacancyRespond();
      vacancyWithVacancyRespond.vacancy = this._vacancy;
      vacancyWithVacancyRespond.vacancyRespond = this.selectedVacancyRespond;
      if (this.offerType === OfferEnum.Alternative) {
        await this.offerService.sendAlternativeOffer(vacancyWithVacancyRespond);


      } else if (this.offerType === OfferEnum.Decline) {
        await this.offerService.sendDeclineOffer(vacancyWithVacancyRespond);


      } else {
        await this.offerService.sendOffer(vacancyWithVacancyRespond);

      }
      await this.getRespondByVacancyIdFromApi();
    } catch (e: any) {
      this.messageService.add({
        severity: "error",
        summary: "Ошибка...",
        detail: e.error.message,
        life: 5000
      });
    }
  }

  async onDialogSubmit($event: any) {
    this.openDialogVacancyComp = false;
    this.competenceWeightScoreForExpert = [];
    this.selectedEmployee = new Employee();
    this.staffUnit = new StaffUnitDto();
    if ($event) {
      await this.getRespondByVacancyIdFromApi();
    }
  }

  closeDialog() {
    this.visible = false;
  }

  async showExpertCart() {
    this.competenceWeightScoreForExpert = await this.competenceService
        .getVacancyCompetenceScoreForEmployee(this.selectedVacancyRespond.id, this.selectedEmployee.id);
    this.openDialogVacancyComp = true;
    this.staffUnit.employee = this.selectedEmployee;
  }

  protected readonly OfferEnum = OfferEnum;
}
