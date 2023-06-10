import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { User } from "../../dto/User";
import { RequestService } from "../../services/request.service";
import { RolesService } from "../../services/roles.service";
import { UserService } from "../../services/user.service";
import { MessageService } from "primeng/api";
import { PasswordChangeRequest } from "../../dto/PasswordChangeRequest";
import { Router } from "@angular/router";

@Component({
  selector: "app-user-dialog",
  templateUrl: "./user-dialog.component.html",
  styleUrls: ["./user-dialog.component.scss"]
})
export class UserDialogComponent implements OnInit {
  @Input("openDialog") visible: boolean = false;
  @Input("currentUser") currentUser: User = new User();
  @Output() submit = new EventEmitter<any>();
  @Output() reloadUser = new EventEmitter<any>();
  @Output() visibleChange = new EventEmitter<any>();
  @Output() logout = new EventEmitter<any>();
  dialogTitle = "Мои настройки";
  roles: { name: string }[];

  acceptDialogVisible: boolean = false;
  passwordChangeDialogVisible: boolean = false;

  fullNameChangeEnabled = false;
  emailChangeEnabled = false;

  oldPassword: string = "";
  newPassword: string = "";
  newPasswordRepeat: string = "";

  login: string = "";

  constructor(public router: Router,
              private requestService: RequestService,
              private rolesService: RolesService,
              private userService: UserService,
              public messageService: MessageService) {
  }

  async ngOnInit(currentUser?: User) {
    if (!!currentUser) {
      this.currentUser = currentUser;
    }
  }

  async fullNameSave() {
    this.fullNameChangeEnabled = false;
    await this.userService.userDataChange(this.currentUser);
    this.reloadUser.emit();
  }

  fullNameChange() {
    this.fullNameChangeEnabled = true;
  }

  async emailSave() {
    this.emailChangeEnabled = false;
    await this.userService.userDataChange(this.currentUser);
    this.reloadUser.emit();
  }

  emailChange() {
    this.emailChangeEnabled = true;
  }

  async passwordSave() {
    this.passwordChangeDialogVisible = false;
    let success = false;
    let request = new PasswordChangeRequest();
    request.oldPassword = this.oldPassword;
    request.newPassword = this.newPassword;
    request.user = this.currentUser;
    try {
      success = await this.userService.userPasswordChange(request);
    } catch (e) {
      throw e;
    } finally {
      if (success) {
        this.messageService.add({
          severity: "success", 
          summary: "Сохранено",
          detail: "Пароль обновлен", 
          life: 5000
        });
        this.closeDialog();
        await this.router.navigate(['/login']);
        localStorage.removeItem("AUTH");
        this.userService.logout();
      } else {
        this.messageService.add({
          severity: "error", 
          summary: "Ошибка", 
          detail: "Введенный пароль неправильный",
          life: 5000
        });
      }
    }
    this.oldPassword = "";
    this.newPassword = "";
    this.newPasswordRepeat = "";
  }

  passwordChange() {
    this.passwordChangeDialogVisible = true;
  }

  passwordSaveDisabled() {
    return (this.newPassword === "" || this.oldPassword === "" || this.newPassword != this.newPasswordRepeat);
  }

  telegramStatusIsSubscribe() {
    return !!this.currentUser.telegramSubscriber;
  }

  async telegramUnsubscribe() {
    try {
      await this.userService.telegramUnsubscribe(this.currentUser.login);
    } catch (e) {
      //nothing
    } finally {
      this.currentUser = await this.userService.getUser();
      this.reloadUser.emit();
    }
  }

  async telegramUnsubscribeConfirmDialogHide(event: any) {
    this.acceptDialogVisible = false;
    if (event === 0) {
      this.telegramUnsubscribe();
      this.currentUser = await this.userService.getUser();
      // await this.ngOnInit();
    }
  }

  signOut() {
    this.router.navigate(['/login']);
    this.userService.logout();
    this.logout.emit();
    this.visible = false;
  }

  closeDialog() {
    this.visible = false;
    this.submit.emit(false);
  }
}
