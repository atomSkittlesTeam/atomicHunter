import { Component, OnInit, ViewChild } from "@angular/core";
import { UserService } from "./services/user.service";
import { Router } from "@angular/router";
import { MenuItem, MessageService } from "primeng/api";
import { Message } from "./dto/Message";
import { RequestService } from "./services/request.service";
import { interval } from "rxjs";
import { User } from "./dto/User";
import { NotificationService } from "./services/notification.service";
import { UserDialogComponent } from "./dialogs/user-dialog/user-dialog.component";


@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.scss"]
})
export class AppComponent implements OnInit {
  title = "atomichunter-webapp";
  userAuth: boolean = false;
  userTelegramSubscriber: boolean = false;
  userRole: string | undefined = "";
  userLogin: string | null = "";
  userFullName: string | null = "";
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
    public notificationService: NotificationService,
    public messageService: MessageService) {
    // this.getMessagesByTime();
    this.userService.currentUser.subscribe(x => {
      if (typeof x === "string") {
        this.initUser(JSON.parse(x));
      } else if (x instanceof User) {
        this.initUser(x);
      }
    });
  }

  async initUser(user: User) {
    // this.user = user || await this.userService.getUser();
    this.user = await this.userService.getUser();
    if (this.user) {
      this.userAuth = true;
      this.userLogin = this.user.login;
      this.userFullName = this.user.fullName;
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
    if (!!this.user) {
      this.userTelegramSubscriber = await this.userService.getTelegramSubscribeStatus();
    }
    this.messages = await this.notificationService.getNewMessages();
  }

  async closeOneInfo(id: number, idx: number) {
    this.messages = this.removeObjectWithId(id);
    await this.notificationService.messageSetFrontSing([id]);
  }

  async closeAllInfo() {
    let ids = this.messages.map(e => e.id);
    await this.notificationService.messageSetFrontSing(ids);
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

  async reloadUser(event: any) {
    this.user = await this.userService.getUser();
    if (!!this.user) {
      this.userTelegramSubscriber = await this.userService.getTelegramSubscribeStatus();
      this.userAuth = true;
      this.userLogin = this.user.login;
      this.userFullName = this.user.fullName;
      this.userRole = this.user.role;
    }
  }

  async readMessage(message: Message) {
    try {
      let ids = this.messages.map(e => e.id).filter(c => c === message.id);
      await this.notificationService.messageSetFrontSing(ids);
      this.messages = this.messages.filter(e => e.id !== message.id);
    } catch (e: any) {
      this.messageService.add({
        severity: "error",
        summary: "Ошибка...",
        detail: e.error.message
      });
    }
  }
}
