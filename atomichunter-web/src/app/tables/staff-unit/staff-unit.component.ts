import {Component, ViewChild} from '@angular/core';
import {CellClickedEvent, ColDef} from "ag-grid-community";
import {LoadingCellRendererComponent} from "../../platform/loading-cell-renderer/loading-cell-renderer.component";
import {AgGridAngular} from "ag-grid-angular";
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {ConfirmationService, MessageService} from "primeng/api";
import {StaffUnitDto} from "../../dto/StaffUnitDto";
import {StaffUnitService} from "../../services/staff-unit.service";

@Component({
    selector: 'app-staff-unit',
    templateUrl: './staff-unit.component.html',
    styleUrls: ['./staff-unit.component.scss']
})
export class StaffUnitComponent {

    loading: boolean = false;
    filter: boolean = false;
    showArchive = false;
    openDialog: boolean = false;
    dialogEditMode: boolean = false;
    selectedStaff: StaffUnitDto;
    pdfResume: string = "";
    showPdfResume: boolean = false;

    public columnDefs: ColDef[] = [
        {field: 'id', headerName: 'Идентификатор', filter: 'agNumberColumnFilter'},
        {field: 'positionId', headerName: 'Должность', filter: 'agTextColumnFilter'},
        {field: 'employeeId', headerName: 'Номер сотрудника', filter: 'agTextColumnFilter'},
        {field: 'status', headerName: 'Состояние', filter: 'agNumberColumnFilter'},
        {field: 'closeTime', headerName: 'Дата увольнения', filter: 'agTextColumnFilter'},
        // {field: 'releaseDate', headerName: 'Дата поставки' , hide: this.showArchive, cellRenderer: (data: { value: string | number | Date; }) => {
        //         return data.value ? (new Date(data.value)).toLocaleDateString() : '';
        //     }},
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


    constructor(public staffUnitService: StaffUnitService,
                public router: Router,
                public http: HttpClient,
                private confirmationService: ConfirmationService,
                private messageService: MessageService) {
    }

    async onGridReady(grid: any) {
        this.agGrid = grid;
        // await this.getAllVacanciesFromApi();
    }

    // Example of consuming Grid Event
    onCellClicked(e: CellClickedEvent): void {
        this.selectedStaff = e.data;
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

    async getAllStaffUnitsFromApi() {
        this.agGrid.api.showLoadingOverlay();
        this.rowData = await this.staffUnitService.getStaffUnits();
        this.loading = false;
    }

    createVacancy() {
        this.openDialog = true;
        this.dialogEditMode = false
    }

    updateVacancy() {
        if (this.selectedStaff) {
            this.openDialog = true;
            this.dialogEditMode = true;
        }
    }


    async showArchivePressed() {
        if (this.agGrid) {
            this.agGrid.columnApi.setColumnVisible('archive', this.showArchive);
        }
    }
}
