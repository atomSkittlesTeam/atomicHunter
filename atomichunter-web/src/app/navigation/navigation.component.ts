import {Component} from '@angular/core';

@Component({
    selector: 'app-navigation',
    templateUrl: './navigation.component.html',
    styleUrls: ['./navigation.component.scss']
})
export class NavigationComponent {
    login: any = ""
    userAuth: boolean = false;
    userRole: string = '';

    constructor() {
        // this.userAuth = this.authService.userAuth;
        // this.userRole = this.authService.userRole;

    }


    ngOnInit(): void {
        this.login = localStorage.getItem("LOGIN");
    }
}
