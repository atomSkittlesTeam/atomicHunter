import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import {UserService} from "../services/user.service";

@Injectable({
    providedIn: 'root'
})
export class AuthGuard {


    constructor(private userService: UserService, private router: Router) {

    }

    canActivate(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

        if (!this.userService.currentUserValue) {
            console.log("Не прошел проверку((");
            this.router.navigate(['/login']);
            return false;
        }

        return true;
    }


}
