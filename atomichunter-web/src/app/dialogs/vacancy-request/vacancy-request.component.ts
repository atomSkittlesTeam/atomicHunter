import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Vacancy} from "../../dto/Vacancy";
import {VacancyRequest} from "../../dto/VacancyRequest";

@Component({
  selector: 'app-vacancy-request',
  templateUrl: './vacancy-request.component.html',
  styleUrls: ['./vacancy-request.component.scss']
})
export class VacancyRequestComponent {

  @Input("openDialog") visible: boolean = false;
  @Input("item") item: VacancyRequest = new VacancyRequest();
  @Input("editMode") editMode: boolean;
  @Output() submit = new EventEmitter<any>();
  @Output() visibleChange = new EventEmitter<any>();
  dialogTitle = "Заведение собрания";

  constructor() {
  }


  closeDialog($event?: any) {
    this.submit.emit($event);
    this.visible = false;
  }

  async onSubmit($event?: any) {
    console.log(this.item)

    if ($event !== null) { // null передается, если закрыть форму без сохранения на крестик
      if (this.editMode) {
      } else {
      }
    }
    this.submit.emit($event);
    this.visible = false;
  }

  getSaveLabel() {
    return this.editMode ? "Обновить" : "Создать";
  }
}
