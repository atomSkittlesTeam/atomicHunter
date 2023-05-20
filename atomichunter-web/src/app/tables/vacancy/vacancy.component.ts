import { HttpClient } from '@angular/common/http';
import { Component, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { AgGridAngular } from 'ag-grid-angular';
import { CellClickedEvent, ColDef } from 'ag-grid-community';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Vacancy } from 'src/app/dto/Vacancy';
import { LoadingCellRendererComponent } from 'src/app/platform/loading-cell-renderer/loading-cell-renderer.component';
import { VacancyService } from 'src/app/services/vacancy.service';

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

  public columnDefs: ColDef[] = [
    {field: 'id', headerName: 'Идентификатор'},
    {field: 'number', headerName: 'Номер заказа'},
    {field: 'description', headerName: 'Описание'},
    {field: 'priority', headerName: 'Приоритет'},
    {field: 'requestDate', headerName: 'Дата заказа', cellRenderer: (data: { value: string | number | Date; }) => {
            return data.value ? (new Date(data.value)).toLocaleDateString() : '';
        }},
    {field: 'releaseDate', headerName: 'Дата поставки' , hide: this.showArchive, cellRenderer: (data: { value: string | number | Date; }) => {
            return data.value ? (new Date(data.value)).toLocaleDateString() : '';
        }},
    {field: 'archive', headerName: 'Архив', hide: !this.showArchive, cellRenderer: (params: { value: any; }) => {
            return `<input disabled="true" type='checkbox' ${params.value ? 'checked' : ''} />`;
        } }
  ];

  public loadingCellRenderer: any = LoadingCellRendererComponent;
  public loadingCellRendererParams: any = {
    loadingMessage: 'One moment please...',
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
        message: 'Отправить позицию в архив?',
        accept: async () => {
            try {
                await this.vacancyService.archiveVacancy(this.selectedVacancy.id);
                this.messageService.add({
                    severity: 'success',
                    summary: 'Успех!',
                    detail: 'Вакансия переведена в архив',
                });
                await this.getAllVacanciesFromApi();
            } catch (e: any) {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Ошибка...',
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
        this.agGrid.columnApi.setColumnVisible('archive', this.showArchive);
    }
    await this.getAllVacanciesFromApi();
  }

}