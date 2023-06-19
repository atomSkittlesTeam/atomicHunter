import {HttpClient} from '@angular/common/http';
import {Component, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {AgGridAngular} from 'ag-grid-angular';
import {CellClickedEvent, ColDef, IDateFilterParams} from 'ag-grid-community';
import {ConfirmationService, MessageService} from 'primeng/api';
import {Vacancy} from 'src/app/dto/Vacancy';
import {LoadingCellRendererComponent} from 'src/app/platform/loading-cell-renderer/loading-cell-renderer.component';
import {VacancyService} from 'src/app/services/vacancy.service';
import {StaffUnitDto} from "../../dto/StaffUnitDto";
import {DatePipe} from "@angular/common";
import {VacancyRespond} from "../../dto/VacancyRespond";

@Component({
    selector: 'app-vacancy',
    templateUrl: './vacancy.component.html',
    styleUrls: ['./vacancy.component.scss'],
    providers: [DatePipe]
})
export class VacancyComponent {

    loading: boolean = false;
    filter: boolean = false;
    showArchive = false;
    showClose = false;
    openDialog: boolean = false;
    dialogEditMode: boolean = false;
    selectedVacancy: Vacancy;
    pdfResume: string = "";
    staffUnit: StaffUnitDto = new StaffUnitDto();
    showPdfResume: boolean = false;
    reportDialogVisible: boolean = false;
    additionalInformationForReport: string = "";
    allRespondsWithInterview: VacancyRespond[] = [];
    selectedRespondForClose: VacancyRespond;
    dialogForClose: boolean = false;

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
        {field: 'id', headerName: 'Идентификатор', filter: 'agNumberColumnFilter'},
        {field: 'position.name', headerName: 'Должность', filter: 'agTextColumnFilter'},
        {field: 'conditions', headerName: 'Условия', filter: 'agTextColumnFilter'},
        {field: 'responsibilities', headerName: 'Обязаности', filter: 'agTextColumnFilter'},
        {field: 'requirements', headerName: 'Требования', filter: 'agTextColumnFilter'},
        {
            field: 'archive', headerName: 'Архив', hide: !this.showArchive,
            cellRenderer: (params: { value: any; }) => {
                return `<input disabled="true" type='checkbox' ${params.value ? 'checked' : ''} />`;
            }
        },
        {
            field: 'closed', headerName: 'Закрыта', hide: !this.showArchive,
            cellRenderer: (params: { value: any; }) => {
                return `<input disabled="true" type='checkbox' ${params.value ? 'checked' : ''} />`;
            }
        },
        {
            field: 'createInstant',
            headerName: 'Дата создания',
            filterParams: this.filterParams,
            filter: 'agDateColumnFilter',
            cellRenderer: (data: { value: number }) => {
                return data.value ? this.datePipe.transform(data.value * 1000, 'dd.MM.yyyy') : '';
            }
        },
        {
            field: 'modifyInstant',
            headerName: 'Дата последнего редактирования',
            filterParams: this.filterParams,
            filter: 'agDateColumnFilter',
            cellRenderer: (data: { value: number }) => {
                return data.value ? this.datePipe.transform(data.value * 1000, 'dd.MM.yyyy') : '';
            }
        },
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


    // Data that gets displayed in the grid
    public rowData!: any[];

    @ViewChild(AgGridAngular) agGrid!: AgGridAngular;
    public overlayLoadingTemplate =
        '<span class="ag-overlay-loading-center">Скоро все появится, подождите еще немного...</span>';

    constructor(public vacancyService: VacancyService,
                public router: Router,
                private datePipe: DatePipe,
                public http: HttpClient,
                private confirmationService: ConfirmationService,
                private messageService: MessageService) {
    }

    async getAllVacanciesFromApi() {
        this.agGrid.api.showLoadingOverlay();
        const vacancies = await this.vacancyService.getVacancies(this.showArchive, this.showClose);
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
        if (this.selectedVacancy) {
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
                    await this.vacancyService.archiveVacancy(this.selectedVacancy.id);
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Успех!',
                        detail: 'Вакансия переведена в архив',
                        life: 5000
                    });
                    await this.getAllVacanciesFromApi();
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

    async getAllRespondsWithInterview() {
        this.allRespondsWithInterview = await this.vacancyService.getVacancyRespondsByIds([this.selectedVacancy.id],
            false);
    }

    async closeVacancy() {
        this.dialogForClose = false;
        try {
            await this.vacancyService.closeVacancy(this.selectedVacancy.id, this.selectedRespondForClose.id);
            this.messageService.add({
                severity: 'success',
                summary: 'Успех!',
                detail: 'Вакансия закрыта',
                life: 5000
            });
            await this.getAllVacanciesFromApi();
        } catch (e: any) {
            this.messageService.add({
                severity: 'error',
                summary: 'Ошибка...',
                detail: e.error.message,
                life: 5000
            });
        }
    }

    isArchivedVacancy() {
        return !this.selectedVacancy || this.selectedVacancy.archive;
    }

    isClosedVacancy() {
        return !this.selectedVacancy || this.selectedVacancy.closed;
    }

    async showArchivePressed() {
        if (this.agGrid) {
            this.agGrid.columnApi.setColumnVisible('archive', this.showArchive);
        }
        await this.getAllVacanciesFromApi();
    }

    async showClosePressed() {
        if (this.agGrid) {
            this.agGrid.columnApi.setColumnVisible('closed', this.showClose);
        }
        await this.getAllVacanciesFromApi();
    }

    async onDialogSubmit($event: any) {
        this.openDialog = false;
        if ($event) {
            await this.getAllVacanciesFromApi();
        }
    }

    pdfResumeAssign(event: string) {
        this.pdfResume = event;
        window.open(this.pdfResume, '_blank');
    }

    async createVacancyReport() {
        this.reportDialogVisible = false;
        if (!!this.selectedVacancy?.id) {
            let pathToReport: string[] = [];
            try {
                pathToReport = await this.vacancyService.createVacancyReport(this.selectedVacancy.id,
                    this.additionalInformationForReport);
                const report = await this.vacancyService.getVacancyFileReport(this.selectedVacancy.id,
                    pathToReport[0]);
            } catch (e) {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Ошибка!',
                    detail: 'Описание вакансии недоступно! Выберите вакансию, затем попробуйте ещё раз.',
                    life: 5000
                });
            } finally {
                this.additionalInformationForReport = "";
            }
        } else {
            this.messageService.add({
                severity: 'error',
                summary: 'Ошибка!',
                detail: 'Описание вакансии недоступно! Выберите вакансию, затем попробуйте ещё раз.',
                life: 5000
            });
        }
    }
}
