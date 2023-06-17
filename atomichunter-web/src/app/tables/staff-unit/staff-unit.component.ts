import {Component, ViewChild} from '@angular/core';
import {CellClickedEvent, ColDef} from "ag-grid-community";
import {LoadingCellRendererComponent} from "../../platform/loading-cell-renderer/loading-cell-renderer.component";
import {AgGridAngular} from "ag-grid-angular";
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {MessageService} from "primeng/api";
import {StaffUnitDto} from "../../dto/StaffUnitDto";
import {OrgStructService} from "../../services/org-struct.service";
import {Vacancy} from "../../dto/Vacancy";
import {VacancyService} from "../../services/vacancy.service";
import {Position} from "../../dto/Position";
import {StatusEnum} from "../../dto/status-enum";

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
    selectedVacancy: Vacancy;
    position: Position;


    public columnDefs: ColDef[] = [
        // {field: 'id', headerName: 'Идентификатор', filter: 'agNumberColumnFilter'},
        {field: 'status', headerName: 'Статус', filter: 'agNumberColumnFilter', cellRenderer: (data: { value: any }) => {
                return !data.value ? " " : this.getEnum(data.value);
            }},
        {field: 'closeTime', headerName: 'Дата закрытия', filter: 'agTextColumnFilter'},
        {field: 'employee.lastName', headerName: 'Фамилия', filter: 'agTextColumnFilter'},
        {field: 'employee.firstName', headerName: 'Имя', filter: 'agTextColumnFilter'},
        {field: 'position.name', headerName: 'Должность', filter: 'agTextColumnFilter'},
        {field: 'vacancyId', headerName: 'id вакансии', filter: 'agTextColumnFilter'}
    ];

    public loadingCellRenderer: any = LoadingCellRendererComponent;
    public loadingCellRendererParams: any = {
        loadingMessage: 'Подождите еще немного...',
    };

    getEnum(type: string) {
        console.log(typeof  type, type, 'fafwf')
        switch (type) {
            case 'Opened':
                return 'Открыто'
            case 'Closed':
                return 'Закрыто'
            case 'Pending':
                return 'Ожидание'
            default:
                return '';
        }
    }

    vacancyCreationDisabled() {
        return this.selectedStaff === null
            || this.selectedStaff === undefined
            || this.selectedStaff.id === null
            || this.selectedStaff.status !== StatusEnum.Opened;
    }

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
                private vacancyService: VacancyService,
                private messageService: MessageService) {
    }

    async onGridReady(grid: any) {
        this.agGrid = grid;
        await this.getAllStaffUnitsFromApi();
    }

    // Example of consuming Grid Event
    onCellClicked(e: CellClickedEvent): void {
        this.selectedStaff = e.data;
        console.log(this.selectedStaff);
        if (this.selectedStaff?.status === StatusEnum.Opened) {
            this.selectedVacancy = new Vacancy();
            this.position = this.selectedStaff.position;
        }
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
        this.rowData = await this.orgStructService.getStaffUnits();
        this.loading = false;
    }

    createVacancy() {
        this.openDialog = true;
        this.dialogEditMode = false
    }

    async onDialogSubmit($event: any) {
        this.openDialog = false;
        if ($event) {
            await this.getAllStaffUnitsFromApi();
        }
    }

    protected readonly StatusEnum = StatusEnum;
}
