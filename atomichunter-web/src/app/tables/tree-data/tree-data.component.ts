import {Component, ViewChild} from '@angular/core';
import {CellClickedEvent, ColDef, GetDataPath} from "ag-grid-community";
import {LoadingCellRendererComponent} from "../../platform/loading-cell-renderer/loading-cell-renderer.component";
import {AgGridAngular} from "ag-grid-angular";
import {getData} from "./data";
import 'ag-grid-enterprise';

@Component({
  selector: 'app-tree-data',
  templateUrl: './tree-data.component.html',
  styleUrls: ['./tree-data.component.scss']
})
export class TreeDataComponent {
  private filter: boolean = false;
  loading: boolean = false;
  public rowData: any[] | null = getData();


  public loadingCellRendererParams: any = {
    loadingMessage: 'One moment please...',
  };
  public loadingCellRenderer: any = LoadingCellRendererComponent;

  public overlayLoadingTemplate =
      '<span class="ag-overlay-loading-center">Скоро все появится, подождите еще немного...</span>';

  public groupDefaultExpanded = -1;

  public defaultColDef: ColDef = {
    editable: false,
    sortable: true,
    flex: 1,
    minWidth: 100,
    filter: true,
    resizable: true,
    floatingFilter: this.filter,
  };

  public columnDefs: ColDef[] = [
    // we're using the auto group column by default!
    { field: 'jobTitle' },
    { field: 'employmentType' },
  ];

  public autoGroupColumnDef: ColDef = {
    headerName: 'Organisation Hierarchy',
    minWidth: 300,
    cellRendererParams: {
      suppressCount: true,
    },
  };

  @ViewChild(AgGridAngular) agGrid!: AgGridAngular;


  public getDataPath: GetDataPath = (data: any) => {
    return data.orgHierarchy;
  };

  onGridReady(grid: any) {
    this.agGrid = grid.api;
  }
  onCellClicked(e: CellClickedEvent): void {
    console.log(e.data)
  }
}
