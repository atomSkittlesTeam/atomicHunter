import { HttpClient } from '@angular/common/http';
import { Component, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { AgGridAngular } from 'ag-grid-angular';
import { CellClickedEvent, ColDef } from 'ag-grid-community';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Vacancy } from 'src/app/dto/Vacancy';
import { LoadingCellRendererComponent } from 'src/app/platform/loading-cell-renderer/loading-cell-renderer.component';
import { VacancyService } from 'src/app/services/vacancy.service';
import {StaffUnitDto} from "../../dto/StaffUnitDto";

@Component({
  selector: 'app-vacancy',
  templateUrl: './vacancy.component.html',
  styleUrls: ['./vacancy.component.scss']
})
export class VacancyComponent {

  loading: boolean = false;
  filter: boolean = false;
  showArchive = false;
  openDialog: boolean = false;
  dialogEditMode: boolean = false;
  selectedVacancy: Vacancy;
  pdfResume: string = "";
  staffUnit: StaffUnitDto = new StaffUnitDto();
  showPdfResume: boolean = false;

  public columnDefs: ColDef[] = [
    {field: 'id', headerName: 'Идентификатор', filter: 'agNumberColumnFilter'},
    {field: 'position.name', headerName: 'Должность', filter: 'agTextColumnFilter'},
    {field: 'conditions', headerName: 'Условия', filter: 'agTextColumnFilter'},
    {field: 'responsibilities', headerName: 'Обязаности', filter: 'agTextColumnFilter'},
    {field: 'requirements', headerName: 'Условия', filter: 'agTextColumnFilter'},
    // {field: 'releaseDate', headerName: 'Дата поставки' , hide: this.showArchive, cellRenderer: (data: { value: string | number | Date; }) => {
    //         return data.value ? (new Date(data.value)).toLocaleDateString() : '';
    //     }},
    {field: 'archive', headerName: 'Архив', hide: !this.showArchive,
            cellRenderer: (params: { value: any; }) => {
            return `<input disabled="true" type='checkbox' ${params.value ? 'checked' : ''} />`;
        }},
    {field: 'createInstant', headerName: 'Дата создания', filter: 'agTextColumnFilter',
          cellRenderer: (data: {value: number}) => {
          return data.value ? new Date(data.value * 1000).toLocaleDateString()
            + ' ' + new Date(data.value * 1000).toLocaleTimeString() : '';
      }},
    {field: 'modifyInstant', headerName: 'Дата последнего редактирования', filter: 'agTextColumnFilter',
          cellRenderer: (data: {value: number}) => {
          return data.value ? new Date(data.value * 1000).toLocaleDateString()
            +  ' ' + new Date(data.value * 1000).toLocaleTimeString() : '';
    }},
  ];

  public loadingCellRenderer: any = LoadingCellRendererComponent;
  public loadingCellRendererParams: any = {
    loadingMessage: 'Подождите еще немного...',
  };

  // DefaultColDef sets props common to all Columns
  public defaultColDef: ColDef = {
      editable: false,
      sortable: true,
      flex: 1,
      minWidth: 100,
      filter: true,
      resizable: true,
      floatingFilter: this.filter,
  };


  // Data that gets displayed in the grid
  public rowData!: any[];

  @ViewChild(AgGridAngular) agGrid!: AgGridAngular;
  public overlayLoadingTemplate =
      '<span class="ag-overlay-loading-center">Скоро все появится, подождите еще немного...</span>';

  constructor(public vacancyService: VacancyService,
    public router: Router,
    public http: HttpClient,
    private confirmationService: ConfirmationService,
    private messageService: MessageService) {}

  async getAllVacanciesFromApi() {
    this.agGrid.api.showLoadingOverlay();
    const vacancies = await this.vacancyService.getVacancies(this.showArchive);
    this.rowData = vacancies;
    this.loading = false;
  }

  async onGridReady(grid: any) {
    this.agGrid = grid;
    await this.getAllVacanciesFromApi();
  }

  // Example of consuming Grid Event
  onCellClicked(e: CellClickedEvent): void {
    this.selectedVacancy = e.data;
  }

  showFilter() {
    this.filter = !this.filter;
    const columnDefs = this.agGrid.api.getColumnDefs();
    if (columnDefs) {
        columnDefs.forEach((colDef:any, index:number)=> {
            colDef.floatingFilter = this.filter;
        });
        this.agGrid.api.setColumnDefs(columnDefs);
        this.agGrid.api.refreshHeader();
    }
  }

  createVacancy() {
    this.openDialog = true;
    this.dialogEditMode = false
  }

  updateVacancy() {
    if (this.selectedVacancy) {
        this.openDialog = true;
        this.dialogEditMode = true;
    }
  }

  archiveVacancy() {
    this.confirmationService.confirm({
        key: "vacancy-archive",
        message: 'Отправить позицию в архив?',
        accept: async () => {
            try {
                await this.vacancyService.archiveVacancy(this.selectedVacancy.id);
                this.messageService.add({
                    severity: 'success',
                    summary: 'Успех!',
                    detail: 'Вакансия переведена в архив',
                    life: 5000
                });
                await this.getAllVacanciesFromApi();
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

  async showArchivePressed() {
    if (this.agGrid) {
        this.agGrid.columnApi.setColumnVisible('archive', this.showArchive);
    }
    await this.getAllVacanciesFromApi();
  }

  async onDialogSubmit($event: any) {
    this.openDialog = false;
    if ($event) {
        await this.getAllVacanciesFromApi();
    }
  }

  pdfResumeAssign(event: string) {
    this.pdfResume = event;
    window.open(this.pdfResume, '_blank');
  }

  async createVacancyReport() {
      if (!!this.selectedVacancy?.id) {
          let pathToReport: string[] = [];
          try {
              pathToReport = await this.vacancyService.createVacancyReport(this.selectedVacancy.id);
              const report = await this.vacancyService.getVacancyFileReport(this.selectedVacancy.id, 
                pathToReport[0]);
          } catch (e) {
              this.messageService.add({
                  severity: 'error',
                  summary: 'Ошибка!',
                  detail: 'Описание вакансии недоступно! Выберите вакансию, затем попробуйте ещё раз.',
                  life: 5000
              });
          }
      } else {
          this.messageService.add({
              severity: 'error',
              summary: 'Ошибка!',
              detail: 'Описание вакансии недоступно! Выберите вакансию, затем попробуйте ещё раз.',
              life: 5000
          });
      }
  }
}
