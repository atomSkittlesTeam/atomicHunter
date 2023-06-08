import { Component, EventEmitter, Input, Output, ViewChild } from "@angular/core";
import { Vacancy } from "../../dto/Vacancy";
import { VacancyRespond } from "../../dto/VacancyRespond";
import { CellClickedEvent, ColDef, GridReadyEvent } from "ag-grid-community";
import { AgGridAngular } from "ag-grid-angular";
import { ConfirmationService, MenuItem, MessageService } from "primeng/api";
import { LoadingCellRendererComponent } from "../../platform/loading-cell-renderer/loading-cell-renderer.component";
import { VacancyService } from "../../services/vacancy.service";
import { InviteService } from "src/app/services/invite.service";
import { VacancyWithVacancyRespond } from "../../dto/VacancyWithVacancyRespond";

@Component({
  selector: "app-vacancy-respond",
  templateUrl: "./vacancy-respond.component.html",
  styleUrls: ["./vacancy-respond.component.scss"]
})
export class VacancyRespondComponent {
  meetingItems: any = [];
  offerItems: any = [];
  items: MenuItem[];
  openDialog: boolean = false;
  dialogEditMode: boolean = false;

  constructor(private confirmationService: ConfirmationService,
              private messageService: MessageService,
              private vacancyService: VacancyService,
              private inviteService: InviteService) {
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

  public overlayLoadingTemplate = "<div class=\"loading-text\"> <span>L</span> <span>O</span> <span>A</span> <span>D</span> <span>I</span> <span>N</span> <span>G</span> </div> ";

  createVacancy() {
    this.openDialog = true;
    this.dialogEditMode = false;
  }

  renderMenu() {
    this.items = [
      {
        label: "Отклики",
        items: [
          {
            label: "Отправить на собеседование",
            disabled: !this.selectedVacancyRespond || !this.selectedVacancyRespond.id,
            icon: "pi pi-envelope",
            command: () => {
              if (this.selectedVacancyRespond.id) {
                this.inviteToInterview();
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
                this.sendOffer();
              }
            }
          }
        ]
      }
    ];
  }

  async onDialogSubmit($event: any) {
    this.openDialog = false;
    if ($event) {
      await this.getRespondByVacancyIdFromApi();
    }
  }

  public columnDefs: ColDef[] = [
    { field: "id", headerName: "Идентификатор", filter: "agTextColumnFilter" },
    { field: "vacancyId", headerName: "Номер вакансии", filter: "agTextColumnFilter" },
    { field: "coverLetter", headerName: "Сопроводительное письмо", filter: "agTextColumnFilter" },
    { field: "email", headerName: "Email", filter: "agTextColumnFilter" },
    {
      field: "interviewInviteAccepted",
      headerName: "Согласен на собеседование",
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
            detail: "Позиция переведена в архив"
          });
          await this.getRespondByVacancyIdFromApi();
        } catch (e: any) {
          this.messageService.add({
            severity: "error",
            summary: "Ошибка...",
            detail: e.error.message
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

  async inviteToInterview() {
    this.openDialog = true;
    this.dialogEditMode = false;
  }


  async sendOffer() {
    try {
      let vacancyWithVacancyRespond = new VacancyWithVacancyRespond();
      vacancyWithVacancyRespond.vacancy = this._vacancy;
      vacancyWithVacancyRespond.vacancyRespond = this.selectedVacancyRespond;
      await this.inviteService.sendOffer(vacancyWithVacancyRespond);
      this.messageService.add({
        severity: "success",
        summary: "Успех!",
        detail: "Оффер отправлен!"
      });
      await this.getRespondByVacancyIdFromApi();
    } catch (e: any) {
      this.messageService.add({
        severity: "error",
        summary: "Ошибка...",
        detail: e.error.message
      });
    }
  }
}
