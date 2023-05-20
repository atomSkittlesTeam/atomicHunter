import { Component, OnInit, ViewChild } from "@angular/core";
import {UserService} from "./services/user.service";
import {Router} from "@angular/router";
import {MenuItem} from "primeng/api";
import {Message} from "./dto/Message";
import {RequestService} from "./services/request.service";
import {interval} from "rxjs";
import {User} from "./dto/User";
import {NotificationService} from "./services/notification.service";
import { DialogComponent } from "./platform/dialog/dialog.component";
import { UserDialogComponent } from "./registration/user-dialog/user-dialog.component";


@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
    title = 'atomichunter-webapp';
    userAuth: boolean = false;
    userTelegramSubscriber: boolean = false;
    userRole: string | undefined = '';
    userLogin: string | null = '';
    items: MenuItem[] = [];
    telegramBotIsEnable: boolean = false;
    displayUserDialog = false;


    messages: Message[] = [];

    display: boolean = false;
    user: User;
    isActiveBurger: boolean = false;

    @ViewChild("userDialogComponent") userDialogComponent: UserDialogComponent;

    constructor(
        public router: Router,
        private userService: UserService,
        public requestService: RequestService,
        public notificationService: NotificationService) {
        // this.getMessagesByTime();
        // @ts-ignore
        this.userService.currentUser.subscribe(x => this.initUser(JSON.parse(x)));
    }

    async initUser(user: User) {
        this.user = user || await this.userService.getUser();
        if (this.user) {
            this.userAuth = true;
            this.userLogin = this.user.login;
            this.userRole = this.user.role;
        }
    }

    async showNewPositions() {
        this.display = true;
        this.messages = await this.notificationService.getNewMessages();
    }

    getMessagesByTime() {
        interval(5000).subscribe(async () => {
            this.messages = await this.notificationService.getNewMessages();

        });
    }

    async ngOnInit(): Promise<void> {
        await this.getTelegramBotCondition();
        this.user = await this.userService.getUser();
        this.userTelegramSubscriber = await this.userService.getTelegramSubscribeStatus();
        this.items = [
            {
                icon: 'pi pi-home',
                command: () => {
                    this.router.navigate(['']);
                }
            },
            {
                icon: 'pi pi-sign-in',
                command: () => {
                    this.router.navigate(['/login']);
                    // this.messageService.add({ severity: 'success', summary: 'Update', detail: 'Data Updated' });
                }
            },
            {
                icon: 'pi pi-sign-out',
                command: () => {
                    this.router.navigate(['/registration']);
                    // this.messageService.add({ severity: 'error', summary: 'Delete', detail: 'Data Deleted' });
                }
            }
        ];
        this.messages = await this.notificationService.getNewMessages();
    }

    async closeOneInfo(id: number, idx: number) {
        this.messages = this.removeObjectWithId(id);
        await this.notificationService.messageSetFrontSing([id])
    }

    async closeAllInfo() {
        let ids = this.messages.map(e => e.id)
        await this.notificationService.messageSetFrontSing(ids)
        this.messages = [];
    }

    removeObjectWithId(id: number) {
        const objWithIdIndex = this.messages.findIndex((obj) => obj.id === id);
        if (objWithIdIndex > -1) {
            this.messages.splice(objWithIdIndex, 1);
        }
        return this.messages;
    }

    async toTelegramSubscribe() {
        if (this.user) {
            let link = await this.userService.getTelegramLink();
            window.open(link[0]);
        }
    }

    async getTelegramBotCondition() {
        if (this.user) {
            this.telegramBotIsEnable = await this.userService.getTelegramBotCondition();
        }
    }

    async openUserDialog() {
        await this.userDialogComponent.ngOnInit(this.user);
        this.displayUserDialog = true;
    }

}
