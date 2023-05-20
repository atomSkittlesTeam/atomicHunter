import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Request} from "../../dto/Request";
import {RequestService} from "../../services/request.service";
import {MessageService} from "primeng/api";
import {User} from "../../dto/User";
import {RolesService} from "../../services/roles.service";
import {UserService} from "../../services/user.service";

@Component({
  selector: 'app-admin-dialog',
  templateUrl: './admin-dialog.component.html',
  styleUrls: ['./admin-dialog.component.scss']
})
export class AdminDialogComponent implements OnInit {
  @Input('openDialog') visible: boolean = false;
  @Input('item') item: User = new User();
  @Input('editMode') editMode: boolean;
  @Output() submit = new EventEmitter<any>();
  @Output() visibleChange = new EventEmitter<any>();
  dialogTitle = 'Регистрация заказа';
  roles: { name: string }[];

  constructor(private requestService: RequestService,
              private rolesService: RolesService,
              private userService: UserService,
              public messageService: MessageService) {
  }

  async ngOnInit() {
    this.roles = await this.rolesService.getRoles().then(data => data.map(role => {
      return {name: role.name}
    }));
    if (this.editMode) {
      this.dialogTitle = 'Редактирование пользователя';
    } else {
      this.item = new User();
      this.dialogTitle = 'Регистрация пользователя';
    }
  }

  async onSubmit($event: any) {
    console.log($event, '$event admin');
    this.submit.emit($event);
    this.visible = false;
  }


}
