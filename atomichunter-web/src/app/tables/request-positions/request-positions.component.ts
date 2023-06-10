import {Component, Input, ViewChild} from '@angular/core';
import {CellClickedEvent, ColDef, GridReadyEvent} from 'ag-grid-community';
import {ConfirmationService, MessageService} from 'primeng/api';
import {RequestPosition} from '../../dto/RequestPosition';
import {RequestService} from '../../services/request.service';
import {Request} from "../../dto/Request";
import {AgGridAngular} from "ag-grid-angular";
import {LoadingCellRendererComponent} from "../../platform/loading-cell-renderer/loading-cell-renderer.component";

@Component({
    selector: 'app-request-positions',
    templateUrl: './request-positions.component.html',
    styleUrls: ['./request-positions.component.scss']
})
export class RequestPositionsComponent {

    private _request: Request;
    filter: boolean = false;
    public get request(): Request {
        return this._request;
    }

    @Input('request')
    public set request(value: Request) {
        this._request = value;
        this.getAllRequestPositionsFromApi();
        this.selectedRequestPosition = new RequestPosition();
    }

    selectedRequestPosition: RequestPosition;

    showArchive = false;

    public columnDefs: ColDef[] = [
        {field: 'id', headerName: 'Идентификатор'},
        {field: 'requestId', headerName: 'Ид заказа'},
        {field: 'note', headerName: 'Примечание'},
        {field: 'product.designation', headerName: 'Продукт', width: 250},
        {field: 'archive', headerName: 'Архив', hide: !this.showArchive, cellRenderer: (params: { value: any; }) => {
            return `<input disabled="true" type='checkbox' ${params.value ? 'checked' : ''} />`;
        } }
    ];

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

    styles: {

    }
    // Data that gets displayed in the grid
    public rowData!: any[];

    editMode: boolean = false;
    openDialog: boolean = false;
    @ViewChild(AgGridAngular) agGrid!: AgGridAngular;
    public overlayLoadingTemplate = '<div class="loading-text"> <span>L</span> <span>O</span> <span>A</span> <span>D</span> <span>I</span> <span>N</span> <span>G</span> </div> '
    constructor(public requestService: RequestService,
                private confirmationService: ConfirmationService,
                private messageService: MessageService) {
    }

    public loadingCellRenderer: any = LoadingCellRendererComponent;
    public loadingCellRendererParams: any = {
        loadingMessage: 'Подождите еще немного...',
    };

    async onGridReady(params: GridReadyEvent) {
        if (this.request && this.request.id) {
            await this.getAllRequestPositionsFromApi();
        }
    }

    // Example of consuming Grid Event
    onCellClicked(e: CellClickedEvent): void {
        this.selectedRequestPosition = e.data;
    }

    async getAllRequestPositionsFromApi() {
        if (this.request && this.request.id) {
            this.agGrid.api.showLoadingOverlay();
            const requests = await this.requestService.getRequestPositions(this.request.id, this.showArchive);
            this.rowData = requests;
        }
    }

    showFilter() {
        this.filter = !this.filter;
        const columnDefs = this.agGrid.api.getColumnDefs();
        columnDefs?.forEach((colDef:any, index:number)=> {
            colDef.floatingFilter = this.filter;
        });
        if (columnDefs) {
            this.agGrid.api.setColumnDefs(columnDefs);
        }
        this.agGrid.api.refreshHeader();
    }

    async onDialogSubmit($event: any) {
        this.openDialog = false;
        await this.getAllRequestPositionsFromApi();
    }

    createRequestPosition() {
        this.openDialog = true;
        this.editMode = false;
    }

    updateRequestPosition() {
        this.openDialog = true;
        this.editMode = true;
    }

    archiveRequestPosition() {
        this.confirmationService.confirm({
            message: 'Отправить позицию в архив?',
            accept: async () => {
                try {
                    await this.requestService.archiveRequestPosition(this.selectedRequestPosition.id);
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Успех!',
                        detail: 'Позиция переведена в архив',
                        life: 5000
                    });
                    await this.getAllRequestPositionsFromApi();
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

    async showArchivePressed() {
        if (this.agGrid) {
            this.agGrid.columnApi.setColumnVisible('archive', this.showArchive);
        }
        await this.getAllRequestPositionsFromApi();
    }
}
