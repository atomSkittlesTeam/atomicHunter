import { Component } from '@angular/core';
import {User} from "../dto/User";
import {Router} from "@angular/router";
import {MessageService} from "primeng/api";
import {UserService} from "../services/user.service";

@Component({
  selector: 'app-password-recover-form',
  templateUrl: './password-recover-form.component.html',
  styleUrls: ['./password-recover-form.component.scss']
})
export class PasswordRecoverFormComponent {
  currentState: PasswordRecoverState = PasswordRecoverState.EMAIL_INPUT;
  currentLabel: string = LabelOfCurrentState[this.currentState];
  currentButtonLabel: string = ButtonLabelOfCurrentState[this.currentState];

  passwordRecoverState = PasswordRecoverState;
  labelOfCurrentState = LabelOfCurrentState;
  buttonLabelOfCurrentState = ButtonLabelOfCurrentState;

  title: string = "Восстановление пароля";
  emailForSendRecoverLetter: string = "";
  recoverCode: string = "";
  newPassword: string = "";
  newPasswordRepeat: string = "";

  constructor(public router: Router,
              public messageService: MessageService,
              public userService: UserService) {
    // localStorage.removeItem("AUTH");
    // this.userService.logout();
  }

  async onButtonClick() {
    switch (this.currentState) {
      case PasswordRecoverState.EMAIL_INPUT:
        await this.sendRecoverLetterToEmail(this.emailForSendRecoverLetter);
        break;
      case PasswordRecoverState.RECOVER_CODE_INPUT:
        await this.verifyRecoverCode(this.recoverCode, this.emailForSendRecoverLetter);
        break;
      case PasswordRecoverState.PASSWORD_INPUT:
        await this.saveNewPassword(this.newPassword, this.emailForSendRecoverLetter);
        break;
    }
  }

  async sendRecoverLetterToEmail(email: string) {
    try {
      await this.userService.sendPasswordRecoverLetterToEmail(email);
      this.messageService.add({
        severity: 'success',
        summary: 'Успешно',
        detail: 'Письмо отправлено',
        life: 5000
      });
      this.incrementOrDecrementPasswordRecoverState(true);
    } catch (e) {
      this.messageService.add({
        severity: 'error',
        summary: 'Ошибка',
        detail: 'Не найдена учетная запись с введенным email',
        life: 5000
      });
    } finally {
      // this.incrementOrDecrementPasswordRecoverState(true);
    }
  }

  async verifyRecoverCode(recoverCode: string, email: string) {
    try {
      await this.userService.verifyRecoverCode(recoverCode, email);
      this.messageService.add({
        severity: 'success',
        summary: 'Успешно',
        detail: 'Код восстановления введен верно',
        life: 5000
      });
      this.incrementOrDecrementPasswordRecoverState(true);
    } catch (e) {
      this.messageService.add({
        severity: 'error',
        summary: 'Ошибка',
        detail: 'Код восстановления введен неверно, либо устарел',
        life: 5000
      });
    } finally {
      // this.incrementOrDecrementPasswordRecoverState(true);
    }
  }

  async saveNewPassword(newPassword: string, email: string) {
    try {
      await this.userService.saveNewPassword(newPassword, email);
      this.messageService.add({
        severity: 'success',
        summary: 'Успешно',
        detail: 'Пароль успешно изменен',
        life: 5000
      });
    } catch (e) {
      this.messageService.add({
        severity: 'error',
        summary: 'Ошибка',
        detail: 'Произошла ошибка сохранения пароля',
        life: 5000
      });
    } finally {
      await this.router.navigate(['/login']);
    }
  }

  disableButtonByStateConditions() {
    switch (this.currentState) {
      case PasswordRecoverState.EMAIL_INPUT:
        return !this.emailForSendRecoverLetter || this.emailForSendRecoverLetter == "" || !this.emailForSendRecoverLetter.includes("@");
      case PasswordRecoverState.RECOVER_CODE_INPUT:
        return !this.recoverCode || this.recoverCode == "";
      case PasswordRecoverState.PASSWORD_INPUT:
        return this.newPassword === "" || this.newPasswordRepeat === "" || this.newPassword != this.newPasswordRepeat;
    }
  }

  incrementOrDecrementPasswordRecoverState(increment: boolean) {
    this.currentState += increment ? 1 : -1;
    this.currentLabel = this.labelOfCurrentState[this.currentState];
    this.currentButtonLabel = this.buttonLabelOfCurrentState[this.currentState];
  }
}

enum PasswordRecoverState {
  EMAIL_INPUT, RECOVER_CODE_INPUT, PASSWORD_INPUT
}

enum LabelOfCurrentState {
  "Введите ваш email, введенный при регистрации",
  "Введите номер, полученный в письме",
  "Введите и подтвердите новый пароль"
}

enum ButtonLabelOfCurrentState {
  "Ввести email",
  "Ввести код",
  "Сохранить пароль"
}

