import {Component, ViewChild} from '@angular/core';
import {Place} from "../../dto/Place";
import {CellClickedEvent, ColDef} from "ag-grid-community";
import {LoadingCellRendererComponent} from "../../platform/loading-cell-renderer/loading-cell-renderer.component";
import {AgGridAngular} from "ag-grid-angular";
import {PlaceService} from "../../services/place.service";
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {ConfirmationService, MessageService} from "primeng/api";

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.scss']
})
export class CalendarComponent {
  loading: boolean = false;
  filter: boolean = false;
  showArchive = false;
  dialogVisible: boolean = false;
  dialogEditMode: boolean = false;
  selectedPlace: Place = new Place();

  public columnDefs: ColDef[] = [
    {field: 'id', headerName: 'Идентификатор', filter: 'agNumberColumnFilter'},
    {field: 'name', headerName: 'Название площадки', filter: 'agTextColumnFilter'},
    {field: 'archive', headerName: 'Архив', hide: !this.showArchive,
      cellRenderer: (params: { value: any; }) => {
        return `<input disabled="true" type='checkbox' ${params.value ? 'checked' : ''} />`;
      }}
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


  constructor(public placeService: PlaceService,
              public router: Router,
              public http: HttpClient) {
  }

  async onGridReady(grid: any) {
    this.agGrid = grid;
    await this.getAllPlasesFromApi();
  }

  // Example of consuming Grid Event
  onCellClicked(e: CellClickedEvent): void {
    this.selectedPlace = e.data;
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

  async getAllPlasesFromApi() {
    this.agGrid.api.showLoadingOverlay();
    this.rowData = await this.placeService.getPlaces(this.showArchive);
    this.loading = false;
  }

}
