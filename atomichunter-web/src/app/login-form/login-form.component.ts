import {Component} from '@angular/core';
import {User} from "../dto/User";
import {Router} from "@angular/router";
import {MessageService} from "primeng/api";
import {UserService} from "../services/user.service";

@Component({
    selector: 'app-login-form',
    templateUrl: './login-form.component.html',
    styleUrls: ['./login-form.component.scss']
})
export class LoginFormComponent {
    user: any = new User();
    title: string = "Войти";

    constructor(public router: Router, public messageService: MessageService, public userService: UserService) {
        localStorage.removeItem("AUTH");
        this.userService.logout();
    }

    async sendRequest() {
        await this.userService.sendLogin(this.user).catch(() => this.messageService.add({
            severity: 'error',
            summary: 'Ошибка авторизации!',
            detail: 'Произошла ошибка Авторизации, попробуйте еще раз',
            sticky: true
        }));
        return 0;
    }
}
