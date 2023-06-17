import {Component, Input, ViewChild} from '@angular/core';
import {MenuItem, MessageService} from "primeng/api";
import {CellClickedEvent, ColDef} from "ag-grid-community";
import {AgGridAngular} from "ag-grid-angular";
import {CompetenceService} from "../../services/competence.service";
import {CompetenceGroupDto} from "../../dto/CompetenceGroupDto";
import {LoadingCellRendererComponent} from "../../platform/loading-cell-renderer/loading-cell-renderer.component";
import {Competence} from "../../dto/Competence";

@Component({
    selector: 'app-competence',
    templateUrl: './competence.component.html',
    styleUrls: ['./competence.component.scss']
})
export class CompetenceComponent {
    items: MenuItem[];
    openDialog: boolean = false;
    dialogEditMode: boolean = false;

    constructor(public competenceService: CompetenceService,
                private messageService: MessageService) {
    }

    private _competenceGroup: CompetenceGroupDto;
    filter: boolean = false;

    public get competenceGroup(): CompetenceGroupDto {
        return this._competenceGroup;
    }

    @Input("competence-group")
    public set competenceGroup(value: CompetenceGroupDto) {
        this._competenceGroup = value;
        this.getCompetenceByIdFromApi();
        this.selectedCompetence = new Competence();
        // this.renderMenu();
    }

    selectedCompetence: Competence;
    showArchive = false;
    public rowData: any[] = [];

    public columnDefs: ColDef[] = [
        {field: "id", headerName: "Идентификатор", filter: "agNumberColumnFilter"},
        {field: "name", headerName: "Наименование", filter: "agTextColumnFilter"}
    ];

    public loadingCellRenderer: any = LoadingCellRendererComponent;
    public loadingCellRendererParams: any = {
        loadingMessage: "Подождите еще немного..."
    };


    // DefaultColDef sets props common to all Columns
    public defaultColDef: ColDef = {
        editable: false,
        sortable: true,
        flex: 1,
        minWidth: 100,
        filter: true,
        resizable: true,
        floatingFilter: this.filter
    };

    @ViewChild(AgGridAngular) agGrid!: AgGridAngular;

    public overlayLoadingTemplate = "<div class=\"loading-text\"> Загрузка...</div> ";

    showFilter() {
        this.filter = !this.filter;
        const columnDefs = this.agGrid.api.getColumnDefs();
        columnDefs?.forEach((colDef: any, index: number) => {
            colDef.floatingFilter = this.filter;
        });
        if (columnDefs) {
            this.agGrid.api.setColumnDefs(columnDefs);
        }
        this.agGrid.api.refreshHeader();
    }

    async onGridReady(grid: any) {
        this.agGrid = grid;
        await this.getCompetenceByIdFromApi();
    }

    // Example of consuming Grid Event
    onCellClicked(e: CellClickedEvent): void {
        this.selectedCompetence = e.data;
    }

    async getCompetenceByIdFromApi() {
        if (!this.competenceGroup) {
            return;
        }
        this.agGrid.api.showLoadingOverlay();
        this.rowData = await this.competenceService.getCompetencesByCompetenceGroupId(this.competenceGroup?.id);
    }

    createCompetence() {
        this.openDialog = true;
        this.dialogEditMode = false
    }

    updateCompetenceGroup() {
        if (this.selectedCompetence) {
            this.openDialog = true;
            this.dialogEditMode = true;
        }
    }

    async onDialogSubmit($event: any) {
        this.openDialog = false;
        if ($event) {
            await this.getCompetenceByIdFromApi();
        }
    }

}
