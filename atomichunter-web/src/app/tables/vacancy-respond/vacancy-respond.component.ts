import { Component, Input, ViewChild } from "@angular/core";
import { Vacancy } from "../../dto/Vacancy";
import { VacancyRespond } from "../../dto/VacancyRespond";
import { CellClickedEvent, ColDef, GridReadyEvent } from "ag-grid-community";
import { AgGridAngular } from "ag-grid-angular";
import { ConfirmationService, MessageService } from "primeng/api";
import { LoadingCellRendererComponent } from "../../platform/loading-cell-renderer/loading-cell-renderer.component";
import { VacancyService } from "../../services/vacancy.service";
import { InviteService } from "src/app/services/invite.service";

@Component({
  selector: "app-vacancy-respond",
  templateUrl: "./vacancy-respond.component.html",
  styleUrls: ["./vacancy-respond.component.scss"]
})
export class VacancyRespondComponent {
  meetingItems: any = [];
  offerItems: any = [];
  openDialog: boolean = false;
  dialogEditMode: boolean = false;

  constructor(private confirmationService: ConfirmationService,
              private messageService: MessageService,
              private vacancyService: VacancyService,
              private inviteService: InviteService) {
    this.meetingItems = [
      {
        label: "отправить на собес",
        icon: "pi pi-envelope",
        command: () => {
          if (this.selectedVacancyRespond.id) {
            this.inviteToInterview();
          }
        }
      },
      { label: "удалить", icon: "pi pi-trash",
      command: () => {
        if (this.selectedVacancyRespond.id) {
          this.archiveRequestPosition();
        }
      }},
      { separator: true },
      { label: "показать архивные", icon: "pi pi-cog" }
    ];

    this.offerItems = [
      {
        label: "Отправить офер",
        icon: "pi pi-envelope",
        command: () => {
          if (this.selectedVacancyRespond.id) {
            this.sendOffer();
          }
        }
      }
    ];
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
  }

  selectedVacancyRespond: VacancyRespond;
  showArchive = false;
  public rowData: any[] = [];

  public overlayLoadingTemplate = "<div class=\"loading-text\"> <span>L</span> <span>O</span> <span>A</span> <span>D</span> <span>I</span> <span>N</span> <span>G</span> </div> ";

  createVacancy() {
    this.openDialog = true;
    this.dialogEditMode = false;
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

  openRespondDialog() {
    // this.openDialog = true;
    // this.editMode = false;
  }

  archiveRequestPosition() {
    this.confirmationService.confirm({
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
      await this.inviteService.sendOffer(this.selectedVacancyRespond);
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
