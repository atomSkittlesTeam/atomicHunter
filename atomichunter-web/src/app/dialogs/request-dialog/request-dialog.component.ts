import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Request} from '../../dto/Request';
import {RequestService} from "../../services/request.service";
import {MessageService} from "primeng/api";

@Component({
    selector: 'app-request-dialog',
    templateUrl: './request-dialog.component.html',
    styleUrls: ['./request-dialog.component.scss']
})
export class RequestDialogComponent implements OnInit {

    @Input('openDialog') visible: boolean = false;
    @Input('item') item: Request = new Request();
    @Input('editMode') editMode: boolean;
    @Output() submit = new EventEmitter<any>();
    @Output() visibleChange = new EventEmitter<any>();
    dialogTitle = 'Регистрация заказа';

    constructor(private requestService: RequestService,
                public messageService: MessageService) {
    }

    ngOnInit(): void {
        if(this.editMode) {
           this.dialogTitle = 'Редактирование заказа'; 
        } else {
            this.item = new Request();
            this.dialogTitle = 'Регистрация заказа';
        }
    }

    async onSubmit($event: any) {
        if ($event !== null) { // null передается, если закрыть форму без сохранения на крестик
            if (this.editMode) {
                await this.updateRequest($event);
            } else {
                await this.createRequest($event);
            }
        }
        this.submit.emit($event);
        this.visible = false;
        /*
        this.selectedRequest = new Request();
        await this.getAllRequestsFromApi();*/
    }

    async createRequest(request: Request) {
        try {
            const rq = await this.requestService.createRequest(request);
            this.messageService.add({
                severity: "success",
                summary: "Успех!",
                detail: "Заказ создан",
            });
        } catch (e: any) {
            this.messageService.add({
                severity: "error",
                summary: "Ошибка...",
                detail: e.error.message,
            });
        }
    }

    async updateRequest(request: Request) {
        try {
            const rq = await this.requestService.updateRequest(request.id, request);
            this.messageService.add({
                severity: "success",
                summary: "Успех!",
                detail: "Заказ обновлён",
            });
        } catch (e: any) {
            this.messageService.add({
                severity: "error",
                summary: "Ошибка...",
                detail: e.error.message,
            });
        }
    }
}
