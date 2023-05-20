import {Injectable} from '@angular/core';
import {BehaviorSubject, firstValueFrom, Observable} from "rxjs";
import {User} from "../dto/User";
import {ConfigService} from '../config/config.service';
import {Router} from "@angular/router";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {BaseService} from "./base.service";
import {log10} from "chart.js/helpers";
import { PasswordChangeRequest } from "../dto/PasswordChangeRequest";

@Injectable({
    providedIn: 'root'
})
export class UserService extends BaseService {
    private user: User;
    private currentUserSubject: BehaviorSubject<User>;
    public currentUser: Observable<User>;

    constructor(private http: HttpClient,
                public router: Router,
                public override configService: ConfigService) {

        super(configService);
        // @ts-ignore
        this.currentUserSubject = new BehaviorSubject<User>(JSON.parse(localStorage.getItem('currentUser')));
        this.currentUser = this.currentUserSubject.asObservable();
    }

    public get currentUserValue(): User {
        this.user = this.currentUserSubject.value;
        return this.currentUserSubject.value;
    }

    async getUsers() {
        const url = await this.getBackendUrl();
        return await firstValueFrom(this.http.get<User[]>(url + '/user/all'));
    }

    async initUser(user: User) {
        this.user = user;
    }

    async getUser() {
        // if (this.user) {
        //     return this.user;  эта хуйня мне всё ломает
        // } else {
            const url = await this.getBackendUrl();
            if (this.router.url !== '/login' && this.router.url !== '/registration')
                this.user = await firstValueFrom(this.http.post<User>(url + '/user/currentUser', {}));
            return this.user;
        // }
    }

    async sendLogin(user: User) {
        const url = await this.getBackendUrl();
        const headers = new HttpHeaders({Authorization: 'Basic ' + window.btoa(user?.login + ':' + user?.password)});
        return await firstValueFrom(this.http.post<User>(url + '/user/currentUser', {}, {
            headers,
            responseType: 'text' as 'json'
        }))
            .then(async data => {
                console.log(data, 'Пользователь');
                localStorage.setItem("AUTH", window.btoa(user?.login + ':' + user?.password));
                localStorage.setItem('currentUser', JSON.stringify(data));
                this.currentUserSubject.next(data as User);
                await this.router.navigate(['/']);
            });
    }

    logout() {
        localStorage.removeItem('currentUser');
        // @ts-ignore
        this.currentUserSubject.next(null);
    }

    async updateUser(userDto: User) {
        const url = await this.getBackendUrl();
        return await firstValueFrom(this.http.put(url + '/user/update', userDto));
    }

    async registration(login: User | undefined) {
        const url = await this.getBackendUrl();
        localStorage.removeItem("AUTH");
        return await firstValueFrom(this.http.post(url + '/registration', {
            login: login?.login,
            password: login?.password,
            email: login?.email,
            fullName: login?.fullName
        })).then(data => {
            this.router.navigate(['/login']);
        });
    }

    async userDataChange(user: User) {
        const url = await this.getBackendUrl();
        return await firstValueFrom(this.http.post<void>(url + `/user/user-data-change`, user));
    }

    async userPasswordChange(changeRequest: PasswordChangeRequest) {
        const url = await this.getBackendUrl();
        return await firstValueFrom(this.http.post<boolean>(url + `/user/password-change`, changeRequest));
    }

    async getTelegramSubscribeStatus() {
        const url = await this.getBackendUrl();
        let userIsSubscribed = await firstValueFrom(
            this.http.get<boolean>(url + '/user/telegram-subscribe-status'));
        console.log(userIsSubscribed ? "Пользователь подписан на телеграм-бота" :
            "Пользователь не подписан на телеграм-бота")
        return userIsSubscribed;
    }

    async telegramUnsubscribe(login: string) {
        const url = await this.getBackendUrl();
        return await firstValueFrom(this.http.get<void>(url + `/user/telegram-unsubscribe/${login}`));
    }

    async getTelegramLink() {
        const url = await this.getBackendUrl();
        return await firstValueFrom(this.http.get<string[]>(url + '/user/telegram-link'));
    }

    async getTelegramBotCondition() { //true - if enabled
        const url = await this.getBackendUrl();
        return await firstValueFrom(this.http.get<boolean>(url + '/user/telegram-enable'));
    }
}