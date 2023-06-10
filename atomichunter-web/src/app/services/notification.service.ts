import {Injectable} from '@angular/core';
import {firstValueFrom} from "rxjs";
import {Message} from "../dto/Message";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {ConfigService} from "../config/config.service";
import {BaseService} from "./base.service";
import { UserService } from "./user.service";
import { User } from "../dto/User";
import { MessageService } from "primeng/api";

@Injectable({
    providedIn: 'root'
})
export class NotificationService extends BaseService {

    constructor(private http: HttpClient,
                public router: Router,
                public userService: UserService,
                public messageService: MessageService,
                public override configService: ConfigService) {
        super(configService);
    }

    async getNewMessages() {
        const url = await this.getBackendUrl();
        let newMessages: Message[] = [];
        let user = null;
        try {
            user = await this.userService.getUser();
        } catch (e: any) {
            this.messageService.add({severity: "error", 
            summary: "Ошибка...", 
            detail: e.error.message,
            life: 5000
        });
        } finally {
            if (user != null) {
                newMessages = await firstValueFrom(this.http.get<Message[]>(url + `/messages/new-messages/${user.login}`));
            }
        }
        return newMessages;
    }

    async messageSetFrontSing(ids: number[]) {
        const url = await this.getBackendUrl();
        return await firstValueFrom(this.http.post(url + `/messages/message-set-front`, ids));
    }
}
