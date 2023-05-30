import { Component, Input, ViewChild } from "@angular/core";
import { Vacancy } from "../../dto/Vacancy";
import { VacancyRespond } from "../../dto/VacancyRespond";
import { CellClickedEvent, ColDef, GridReadyEvent } from "ag-grid-community";
import { AgGridAngular } from "ag-grid-angular";
import { RequestService } from "../../services/request.service";
import { ConfirmationService, MessageService } from "primeng/api";
import { LoadingCellRendererComponent } from "../../platform/loading-cell-renderer/loading-cell-renderer.component";
import { VacancyService } from "../../services/vacancy.service";

@Component({
  selector: 'app-vacancy-respond',
  templateUrl: './vacancy-respond.component.html',
  styleUrls: ['./vacancy-respond.component.scss']
})
export class VacancyRespondComponent {
  constructor(private confirmationService: ConfirmationService,
              private messageService: MessageService,
              private vacancyService: VacancyService) {
  }

  private _vacancy: Vacancy;
  filter: boolean = false;
  public get vacancy(): Vacancy {
    return this._vacancy;
  }

  @Input('vacancy')
  public set vacancy(value: Vacancy) {
    this._vacancy = value;
    this.getAllRequestPositionsFromApi();
    this.selectedVacancyRespond = new VacancyRespond();
  }

  selectedVacancyRespond: VacancyRespond;
  showArchive = false;
  public rowData: any[] = [];

  public overlayLoadingTemplate = '<div class="loading-text"> <span>L</span> <span>O</span> <span>A</span> <span>D</span> <span>I</span> <span>N</span> <span>G</span> </div> '

  public columnDefs: ColDef[] = [
    {field: 'id', headerName: 'Идентификатор'},
    {field: 'vacancyId', headerName: 'Ид заказа'},
    {field: 'coverLetter', headerName: 'Примечание'},
    // {field: 'product.designation', headerName: 'Продукт', width: 250},
    {field: 'archive', headerName: 'Архив', hide: !this.showArchive, cellRenderer: (params: { value: any; }) => {
        return `<input disabled="true" type='checkbox' ${params.value ? 'checked' : ''} />`;
      } }
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

  async getAllRequestPositionsFromApi() {
    if (this.vacancy && this.vacancy.id) {
      this.agGrid.api.showLoadingOverlay();
      const responds = await this.vacancyService.getVacancyRespondsByIds([this.vacancy.id], this.showArchive);
      this.rowData = responds;
      // const requests = await this.requestService.getRequestPositions(this.request.id, this.showArchive);
      // this.rowData = requests;
    }
  }

  public loadingCellRenderer: any = LoadingCellRendererComponent;
  public loadingCellRendererParams: any = {
    loadingMessage: 'Подождите еще немного...',
  };

  async onGridReady(params: GridReadyEvent) {
    if (this.vacancy && this.vacancy.id) {
      await this.getAllRequestPositionsFromApi();
    }
  }

  onCellClicked(e: CellClickedEvent): void {
    this.selectedVacancyRespond = e.data;
  }

  showFilter() {
    this.filter = !this.filter;
    const columnDefs = this.agGrid.api.getColumnDefs();
    columnDefs?.forEach((colDef:any, index:number)=> {
      colDef.floatingFilter = this.filter;
    });
    // @ts-ignore
    this.agGrid.api.setColumnDefs(columnDefs);
    this.agGrid.api.refreshHeader();
  }

  createRequestPosition() {
    // this.openDialog = true;
    // this.editMode = false;
  }

  archiveRequestPosition() {
    this.confirmationService.confirm({
      message: 'Отправить позицию в архив?',
      accept: async () => {
        try {
          // await this.requestService.archiveRequestPosition(this.selectedVacancyRespond.id);
          this.messageService.add({
            severity: 'success',
            summary: 'Успех!',
            detail: 'Позиция переведена в архив',
          });
          await this.getAllRequestPositionsFromApi();
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
    await this.getAllRequestPositionsFromApi();
  }

}
