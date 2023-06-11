import {Component, OnInit, ViewChild} from '@angular/core';
import {Request} from "../../dto/Request";
import {CellClickedEvent, ColDef, GridReadyEvent} from "ag-grid-community";
import {AgGridAngular} from "ag-grid-angular";
import {RequestService} from "../../services/request.service";
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {ConfirmationService, MessageService} from "primeng/api";

@Component({
  selector: 'app-master-detail-detail',
  templateUrl: './master-detail-detail.component.html',
  styleUrls: ['./master-detail-detail.component.scss']
})
export class MasterDetailDetailComponent implements OnInit{

  request: Request[] = [];
  selectedRequests: Request[] = [];
  selectedRequest: Request;
  dialogEditMode: boolean = false;
  filter: boolean = false;

  userRole: string = '';
  openDialog: boolean = false;
  loading: boolean = false;
  showArchive = true;

  public columnDefs: ColDef[] = [
    {field: 'id', headerName: 'Идентификатор'},
    {field: 'number', headerName: 'Номер заказа'},
    {field: 'description', headerName: 'Описание'},
    {field: 'priority', headerName: 'Приоритет'},
    {field: 'requestDate', headerName: 'Дата заказа', cellRenderer: (data: { value: string | number | Date; }) => {
        return data.value ? (new Date(data.value)).toLocaleDateString() : '';
      }},
    {field: 'releaseDate', headerName: 'Дата поставки' , cellRenderer: (data: { value: string | number | Date; }) => {
        return data.value ? (new Date(data.value)).toLocaleDateString() : '';
      }},
    {field: 'archive', headerName: 'Архив'}
  ];

  // DefaultColDef sets props common to all Columns
  public defaultColDef: ColDef = {
    sortable: true,
    filter: true,
  };

  // Data that gets displayed in the grid
  public rowData!: any[];

  isProductionPlanMode = false;
  @ViewChild(AgGridAngular) agGrid!: AgGridAngular;
  public overlayLoadingTemplate =
      '<span class="ag-overlay-loading-center">Скоро все появится, подождите еще немного...</span>';

  public loadingCellRendererParams: any = {
    loadingMessage: 'Подождите еще немного...',
  };
  constructor(public requestService: RequestService,
              public router: Router,
              public http: HttpClient,
              private confirmationService: ConfirmationService,
              private messageService: MessageService) {
  }

  async ngOnInit() {
    this.loading = true;
    // this.request = await this.requestService.getRequests();
  }

  showFilter() {
    this.filter = !this.filter;
    const columnDefs = this.agGrid.api.getColumnDefs();
    columnDefs?.forEach((colDef:any, index:number)=> {
        colDef.floatingFilter = this.filter;
    });
    if (columnDefs) {
      this.agGrid.api.setColumnDefs(columnDefs);
    }
    this.agGrid.api.refreshHeader();
  }

  async checkRequest(selected: any) {
    if (selected.length == 1) {
      // this.requestPositions = await this.requestService.getRequestPositionById(selected[0].id);
    } else {
      // this.requestPositions = [];
    }
  }

  async refreshDetail() {
    // this.requestPositions = await this.requestService.getRequestPositionById(this.selectedRequests[0].id);
  }

  async refreshMain() {
    this.request = await this.requestService.getRequests(this.showArchive);
  }

  async onGridReady(params: GridReadyEvent) {
    await this.getAllRequestsFromApi();
  }

  async getAllRequestsFromApi() {
    this.agGrid.api.showLoadingOverlay();
    const requests = await this.requestService.getRequests(this.showArchive);
    this.rowData = requests;
    this.loading = false;
  }

  // Example of consuming Grid Event
  onCellClicked(e: CellClickedEvent): void {
    this.selectedRequest = e.data;
  }

  // Example using Grid's API
  clearSelection(): void {
    this.agGrid.api.deselectAll();
  }

  async onDialogSubmit($event: any) {
    this.openDialog = false;
    if ($event) {
      await this.getAllRequestsFromApi();
    }
  }

  createRequest() {
    this.openDialog = true;
    this.dialogEditMode = false
  }

  updateRequest() {
    if (this.selectedRequest) {
      this.openDialog = true;
      this.dialogEditMode = true;
    }
  }

  archiveRequest() {
    this.confirmationService.confirm({
      message: 'Отправить позицию в архив?',
      accept: async () => {
        try {
          await this.requestService.archiveRequest(this.selectedRequest.id);
          this.messageService.add({
            severity: 'success',
            summary: 'Успех!',
            detail: 'Позиция переведена в архив',
            life: 5000
          });
          await this.getAllRequestsFromApi();
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
}
