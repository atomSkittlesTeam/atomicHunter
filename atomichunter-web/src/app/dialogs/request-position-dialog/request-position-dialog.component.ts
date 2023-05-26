import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MessageService } from 'primeng/api';
import { RequestPosition } from '../../dto/RequestPosition';
import { RequestService } from '../../services/request.service';
import {Product} from "../../dto/Product";

@Component({
  selector: 'app-request-position-dialog',
  templateUrl: './request-position-dialog.component.html',
  styleUrls: ['./request-position-dialog.component.scss']
})
export class RequestPositionDialogComponent {

  @Input('openDialog') visible: boolean = false;
  @Input('item') item: RequestPosition = new RequestPosition();
  @Input('editMode') editMode: boolean;
  @Input('requestId') requestId: number;
  @Output() submit = new EventEmitter<any>();
  @Output() visibleChange = new EventEmitter<any>();
  dialogTitle = 'Регистрация позиции заказа';
  products: any[] = [];

  constructor(private requestService: RequestService,
    public messageService: MessageService) {
  }

  async ngOnInit() {
    await this.getAllProductsFromApi();

    if(this.editMode) {
      this.dialogTitle = 'Редактирование позиции заказа'; 
    } else {
       this.item = new RequestPosition();
       this.item.requestId = this.requestId;
       this.item.product = new Product();
       this.dialogTitle = 'Регистрация позиции заказа'; 
    }
  }

  async onSubmit($event: any) {
    if ($event !== null) { // null передается, если закрыть форму без сохранения на крестик
        if (this.editMode) {
            await this.updateRequestPosition($event);
        } else {
            await this.createRequestPosition($event);
        }
    }
    this.submit.emit();
    this.visible = false;
  }

  async createRequestPosition(requestPosition: RequestPosition) {
    try {
        const rq = await this.requestService.createRequestPosition(requestPosition);
        this.messageService.add({
            severity: "success",
            summary: "Успех!",
            detail: "Позиция заказа заведена",
        });
    } catch (e: any) {
        this.messageService.add({
            severity: "error",
            summary: "Ошибка...",
            detail: e.error.message,
        });
    }
  }

  async updateRequestPosition(requestPosition: RequestPosition) {
    try {
        const rq = await this.requestService.updateRequestPosition(requestPosition.id, requestPosition);
        this.messageService.add({
            severity: "success",
            summary: "Успех!",
            detail: "Позиция заказа обновлена",
        });
    } catch (e: any) {
        this.messageService.add({
            severity: "error",
            summary: "Ошибка...",
            detail: e.error.message,
        });
    }
  }

  async getAllProductsFromApi() {
    this.products = await this.requestService.getProducts();
  }
}
