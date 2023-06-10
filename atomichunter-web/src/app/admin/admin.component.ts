import {Component, OnInit, ViewChild} from '@angular/core';
import {User} from "../dto/User";
import {MessageService} from "primeng/api";
import {UserService} from "../services/user.service";
import {CellClickedEvent, ColDef, GridReadyEvent} from "ag-grid-community";
import {AgGridAngular} from "ag-grid-angular";
import {RolesService} from "../services/roles.service";

@Component({
    selector: 'app-admin',
    templateUrl: './admin.component.html',
    styleUrls: ['./admin.component.scss']
})
export class AdminComponent implements OnInit {
    selectedUser: User;
    private filter: boolean = false;
    loading: boolean = false;
    userDto: User = new User();
    user: User[] = [];
    displayDialog: boolean = false;
    roles: { name: string }[] = [];
    telegramBotIsEnable: boolean = false;
    telegramUnsubscribeLabel: string = 'Отписать пользователя от телеграм-бота';

    public loadingCellRendererParams: any = {
        loadingMessage: 'One moment please...',
    };
    public overlayLoadingTemplate =
        '<span class="ag-overlay-loading-center">Скоро все появится, подождите еще немного...</span>';

    public rowData!: any[];
    public columnDefs: ColDef[] = [
        {field: 'login', headerName: 'Логин', filter: "agTextColumnFilter" },
        {field: 'role', headerName: 'Роль', filter: "agTextColumnFilter" },
        {field: 'email', headerName: 'Почта', width: 250, filter: "agTextColumnFilter" },
        {field: 'fullName', headerName: 'ФИО', width: 250, filter: "agTextColumnFilter" },
        {field: 'telegramSubscriber.login', headerName: 'Телеграм', filter: "agTextColumnFilter" }
    ];
    public defaultColDef: ColDef = {
        editable: false,
        sortable: true,
        flex: 1,
        minWidth: 100,
        filter: true,
        resizable: true,
        floatingFilter: this.filter,
    };
    @ViewChild(AgGridAngular) agGrid!: AgGridAngular;


    constructor(public userService: UserService,
                public messageService: MessageService,
                public rolesService: RolesService) {
    }

    async ngOnInit() {
        this.loading = true;
        await this.getTelegramBotCondition();
    }

    async onGridReady(params: GridReadyEvent) {
        await this.getAllUsersFromApi();
    }

    onCellClicked(e: CellClickedEvent): void {
        this.selectedUser = e.data;
    }

    async getAllUsersFromApi() {
        this.agGrid.api.showLoadingOverlay();
        this.rowData = await this.userService.getUsers();
        this.loading = false;
    }

    showDialog() {
        this.displayDialog = true;
        this.userDto = structuredClone(this.selectedUser);
    }

    async telegramBotUnsubscribe() {
        await this.userService.telegramUnsubscribe(this.selectedUser.login);
        this.user = await this.userService.getUsers();
        this.messageService.add({
            severity: 'success',
            summary: 'Успех!',
            detail: 'Пользователь отписан от телеграм-бота',
            life: 5000
        });
        this.getAllUsersFromApi();
    }

    async getTelegramBotCondition() {
        this.telegramBotIsEnable = await this.userService.getTelegramBotCondition();
        if (this.telegramBotIsEnable) {
            this.telegramUnsubscribeLabel = 'Отписать пользователя от телеграм-бота';
        } else {
            this.telegramUnsubscribeLabel = 'Telegram неактивен';
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

    async closeDialog($event?: any) {
        if ($event !== null) {
            try {
                const data = await this.userService.updateUser($event);
                this.selectedUser.role = $event.role;
                this.messageService.add({
                    severity: 'success',
                    summary: 'Успех!',
                    detail: 'Пользователь обновлён!',
                    life: 5000
                });
                await this.getAllUsersFromApi();
            } catch(e: any) {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Ошибка!',
                    detail: e.error.message,
                    life: 5000
                });
            }
        }
        this.displayDialog = false;
    }
}
