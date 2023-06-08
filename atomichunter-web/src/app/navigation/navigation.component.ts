import {Component} from '@angular/core';
import { UserService } from "../services/user.service";
import { User } from "../dto/User";

@Component({
    selector: 'app-navigation',
    templateUrl: './navigation.component.html',
    styleUrls: ['./navigation.component.scss']
})
export class NavigationComponent {
    user: User;
    login: any = ""
    userAuth: boolean = false;
    userRole?: string = '';

    constructor(private userService: UserService) {
        // this.userAuth = this.authService.userAuth;
        // this.userRole = this.authService.userRole;

    }


    ngOnInit(): void {
        this.login = localStorage.getItem("LOGIN");
        this.getUser();
    }

    async getUser() {
        this.user = await this.userService.getUser();
        if (this.user) {
            this.userAuth = true;
            this.userRole = this.user.role;
        }
    }
}
