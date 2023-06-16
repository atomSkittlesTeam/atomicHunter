import {Component, ViewChild} from '@angular/core';
import {CellClickedEvent, ColDef} from "ag-grid-community";
import {LoadingCellRendererComponent} from "../../platform/loading-cell-renderer/loading-cell-renderer.component";
import {AgGridAngular} from "ag-grid-angular";
import {VacancyService} from "../../services/vacancy.service";
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {ConfirmationService, MessageService} from "primeng/api";
import {StaffUnitDto} from "../../dto/StaffUnitDto";

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


    constructor(public vacancyService: VacancyService,
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

    archiveVacancy() {
        this.confirmationService.confirm({
            key: "vacancy-archive",
            message: 'Отправить позицию в архив?',
            accept: async () => {
                try {
                    await this.vacancyService.archiveVacancy(this.selectedStaff.id);
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Успех!',
                        detail: 'Вакансия переведена в архив',
                    });
                    // await this.getAllVacanciesFromApi();
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
    }
}
