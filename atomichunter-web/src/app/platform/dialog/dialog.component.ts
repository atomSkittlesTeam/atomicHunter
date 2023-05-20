import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {MessageService} from "primeng/api";

@Component({
    selector: 'app-dialog',
    templateUrl: './dialog.component.html',
    styleUrls: ['./dialog.component.scss'],
    providers: [FormsModule]
})
export class DialogComponent implements OnInit {

    @Input("visible") visible: any = false;
    @Input("selectedItem") selectedItem: any;
    @Input("title") tittle: string;
    @Input("editMode") editMode: boolean = false;
    @Output() submit = new EventEmitter<any>();

    constructor(public messageService: MessageService) {
    }

    ngOnInit(): void {}

    closeDialog(change: boolean) {
        this.visible = false;
        this.submit.emit(null);
    }

    async save() {
        this.submit.emit(this.selectedItem);
    }

    getSaveLabel() {
        return this.editMode ? "Обновить" : "Создать";
    }
}
