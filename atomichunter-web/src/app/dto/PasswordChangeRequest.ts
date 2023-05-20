import { User } from "./User";

export class PasswordChangeRequest {
  oldPassword: string;
  newPassword: string;
  user: User;

  constructor() {
  }
}
