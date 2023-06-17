import {Component, ViewChild} from '@angular/core';
import {StaffUnitDto} from "../../dto/StaffUnitDto";
import {CellClickedEvent, ColDef} from "ag-grid-community";
import {LoadingCellRendererComponent} from "../../platform/loading-cell-renderer/loading-cell-renderer.component";
import {AgGridAngular} from "ag-grid-angular";
import {OrgStructService} from "../../services/org-struct.service";
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {ConfirmationService, MessageService} from "primeng/api";
import {Employee} from "../../dto/Employee";

@Component({
  selector: 'app-employees',
  templateUrl: './employees.component.html',
  styleUrls: ['./employees.component.scss']
})
export class EmployeesComponent {
  loading: boolean = false;
  filter: boolean = false;
  showArchive = false;
  openDialog: boolean = false;
  dialogEditMode: boolean = false;
  selectedEmployee: Employee;

  public columnDefs: ColDef[] = [
    {field: 'id', headerName: 'Идентификатор', filter: 'agNumberColumnFilter'},
    {field: 'positionId', headerName: 'Должность', filter: 'agTextColumnFilter'},
    {field: 'staffUnitId', headerName: 'Номер штатной единицы', filter: 'agTextColumnFilter'},
    {field: 'firstName', headerName: 'Имя', filter: 'agNumberColumnFilter'},
    {field: 'lastName', headerName: 'Фамилия', filter: 'agNumberColumnFilter'},
    {field: 'email', headerName: 'Электронная почта', filter: 'agTextColumnFilter'}
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
  public rowData!: any[];

  @ViewChild(AgGridAngular) agGrid!: AgGridAngular;
  public overlayLoadingTemplate =
      '<span class="ag-overlay-loading-center">Скоро все появится, подождите еще немного...</span>';


  constructor(public orgStructService: OrgStructService,
              public router: Router,
              public http: HttpClient,
              private confirmationService: ConfirmationService,
              private messageService: MessageService) {
  }

  async onGridReady(grid: any) {
    this.agGrid = grid;
    await this.getAllEmployeesFromApi();
  }

  // Example of consuming Grid Event
  onCellClicked(e: CellClickedEvent): void {
    this.selectedEmployee = e.data;
  }

  showFilter() {
    this.filter = !this.filter;
    const columnDefs = this.agGrid.api.getColumnDefs();
    if (columnDefs) {
      columnDefs.forEach((colDef: any, index: number) => {
        colDef.floatingFilter = this.filter;
      });
      this.agGrid.api.setColumnDefs(columnDefs);
      this.agGrid.api.refreshHeader();
    }
  }

  async getAllEmployeesFromApi() {
    this.agGrid.api.showLoadingOverlay();
    this.rowData = await this.orgStructService.getEmployees();
    this.loading = false;
  }
}
