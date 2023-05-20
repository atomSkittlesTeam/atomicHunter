import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
    selector: 'app-dialog-row',
    templateUrl: './dialog-row.component.html',
    styleUrls: ['./dialog-row.component.scss'],
    providers: []
})
export class DialogRowComponent {

    _item: unknown;
    @Input() type: string;
    @Input() dropDownField: string;
    @Input() title: string;
    @Input() icon: string;
    @Input() dropDown: any[];

    @Input() disabled: boolean = false;

    @Input()
    set item(val: any) {
        if (this.type === 'date' && !!val) {
            this._item = new Date(val)
        } else {
            this._item = val;
        }
    }

    get item() {
        return this._item;
    }

    @Output() itemChange = new EventEmitter<any>();

    constructor() {
    }


}
