import {Component, ViewChild} from '@angular/core';
import {CellClickedEvent, ColDef} from "ag-grid-community";
import {LoadingCellRendererComponent} from "../../platform/loading-cell-renderer/loading-cell-renderer.component";
import {AgGridAngular} from "ag-grid-angular";
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {MessageService} from "primeng/api";
import {CompetenceService} from "../../services/competence.service";
import {CompetenceGroupDto} from "../../dto/CompetenceGroupDto";

@Component({
    selector: 'app-competence-group',
    templateUrl: './competence-group.component.html',
    styleUrls: ['./competence-group.component.scss']
})
export class CompetenceGroupComponent {
    loading: boolean = false;
    filter: boolean = false;
    showArchive: boolean = false;
    openDialog: boolean = false;
    dialogEditMode: boolean = false;
    selectedCompetenceGroup: CompetenceGroupDto;


    public columnDefs: ColDef[] = [
        {field: 'id', headerName: 'Идентификатор', width: 150, filter: 'agNumberColumnFilter'},
        {field: 'name', headerName: 'Наименование группы', filter: 'agTextColumnFilter'},
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


    constructor(public competenceService: CompetenceService,
                public router: Router,
                public http: HttpClient,
                private messageService: MessageService) {
    }

    async onGridReady(grid: any) {
        this.agGrid = grid;
        await this.getAllCompetenceGroupsFromApi();
    }

    // Example of consuming Grid Event
    onCellClicked(e: CellClickedEvent): void {
        this.selectedCompetenceGroup = e.data;
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

    async getAllCompetenceGroupsFromApi() {
        this.agGrid.api.showLoadingOverlay();
        this.rowData = await this.competenceService.getAllCompetenceGroups();
        this.loading = false;
    }

    createCompetenceGroup() {
        this.openDialog = true;
        this.dialogEditMode = false
    }

    updateCompetenceGroup() {
        if (this.selectedCompetenceGroup) {
            this.openDialog = true;
            this.dialogEditMode = true;
        }
    }

    async onDialogSubmit($event: any) {
        this.openDialog = false;
        if ($event) {
            await this.getAllCompetenceGroupsFromApi();
        }
    }

    async showArchivePressed() {
        if (this.agGrid) {
            this.agGrid.columnApi.setColumnVisible('archive', this.showArchive);
        }
    }
}
