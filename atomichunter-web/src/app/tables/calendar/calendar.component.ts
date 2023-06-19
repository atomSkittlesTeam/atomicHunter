import {Component, ViewChild} from '@angular/core';
import {Place} from "../../dto/Place";
import {CellClickedEvent, ColDef, IDateFilterParams} from "ag-grid-community";
import {LoadingCellRendererComponent} from "../../platform/loading-cell-renderer/loading-cell-renderer.component";
import {AgGridAngular} from "ag-grid-angular";
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {InterviewService} from "../../services/interview.service";
import {DatePipe} from "@angular/common";

@Component({
    selector: 'app-calendar',
    templateUrl: './calendar.component.html',
    styleUrls: ['./calendar.component.scss'],
    providers: [DatePipe]
})
export class CalendarComponent {
    loading: boolean = false;
    filter: boolean = false;
    showArchive = false;
    dialogVisible: boolean = false;
    dialogEditMode: boolean = false;
    selectedPlace: Place = new Place();

    filterParams: IDateFilterParams = {
        comparator: (filterLocalDateAtMidnight: Date, cellValue: number) => {
            let dateAsString = this.datePipe.transform(cellValue * 1000, 'dd.MM.yyyy');
            if (dateAsString == null) return -1;

            // let dateParts = dateAsString.split('/');
            let cellDate = dateAsString;

            if (filterLocalDateAtMidnight.toLocaleDateString() === cellDate) {
                return 0;
            }
            if (cellDate !== filterLocalDateAtMidnight.toLocaleDateString()) {
                return -1;
            }
            return 0;
        }
    }

    public columnDefs: ColDef[] = [
        {field: 'interview.vacancyRespond.fullName', headerName: 'Кандидат', filter: 'agNumberColumnFilter'},
        {field: 'vacancy.name', headerName: 'Наименование вакансии', filter: 'agNumberColumnFilter'},
        {field: 'vacancy.staffUnit.position.name', headerName: 'Должность', filter: 'agNumberColumnFilter'},
        {field: 'interview.place.name', headerName: 'Место', filter: 'agTextColumnFilter'},
        {
            field: 'interview.dateStart',
            filterParams: this.filterParams,
            headerName: 'Дата начала собеседования',
            filter: 'agDateColumnFilter',
            cellRenderer: (data: { value: number }) => {
                return data.value ? this.datePipe.transform(data.value * 1000, 'dd.MM.yyyy')
                    + ' ' + new Date(data.value * 1000).toLocaleTimeString() : '';
            }
        },
        {
            field: 'interview.dateEnd',
            filterParams: this.filterParams,
            headerName: 'Дата окончания собеседования',
            filter: 'agDateColumnFilter',
            cellRenderer: (data: { value: number }) => {
                return data.value ? this.datePipe.transform(data.value * 1000, 'dd.MM.yyyy')
                    + ' ' + new Date(data.value * 1000).toLocaleTimeString() : '';
            }
        },
        // {field: 'members', headerName: 'Участники', filter: 'agTextColumnFilter'}
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
    public rowData!: any;

    @ViewChild(AgGridAngular) agGrid!: AgGridAngular;
    public overlayLoadingTemplate =
        '<span class="ag-overlay-loading-center">Скоро все появится, подождите еще немного...</span>';


    constructor(public interviewService: InterviewService,
                public router: Router,
                private datePipe: DatePipe,
                public http: HttpClient) {
    }

    async onGridReady(grid: any) {
        this.agGrid = grid;
        await this.getCalendarFromApi();
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

    async getCalendarFromApi() {
        this.agGrid.api.showLoadingOverlay();
        this.rowData = await this.interviewService.getCalendar(this.showArchive);
        this.loading = false;
    }

    async showArchivePressed() {
      await this.getCalendarFromApi();
    }

    getTableTitle() {
        return "Собеседования" + (this.showArchive ? " прошедшие" : " предстоящие");
    }
 
}
