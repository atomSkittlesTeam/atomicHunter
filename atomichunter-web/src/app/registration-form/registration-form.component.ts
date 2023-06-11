import { Component } from '@angular/core';
import {Router} from "@angular/router";
import {MessageService} from "primeng/api";
import {UserService} from "../services/user.service";
import {User} from "../dto/User";

@Component({
  selector: 'app-registration-form',
  templateUrl: './registration-form.component.html',
  styleUrls: ['./registration-form.component.scss']
})
export class RegistrationFormComponent {
  user: any = new User();
  title: string = "Регистрация";
  buttonLabel: string = "Зарегистрироваться";
  constructor(public router: Router, public messageService: MessageService, public userService: UserService) {
    localStorage.removeItem("AUTH");
    this.userService.logout();
  }

  async sendRequest() {
    await this.userService.registration(this.user).catch(() => this.messageService.add({
      severity: 'error',
      summary: 'Ошибка регистрации',
      detail: 'Произошла ошибка Регистрации',
      life: 5000
    }))
  }
}
