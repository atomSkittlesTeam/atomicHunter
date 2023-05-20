import {TelegramSubscriber} from "./TelegramSubscriber";

export class  User {
 login: string = "";
 email: string = "";
 fullName: string = "";
 password: string = "";
 telegramSubscriber?: TelegramSubscriber = new TelegramSubscriber();
 role?: string;

 constructor() {
 }
}
