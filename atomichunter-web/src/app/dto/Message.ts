import {Data} from "@angular/router";

export class Message {
  id: number;
  customText: string;
  emailSign: boolean;
  frontSign: boolean;

  objectId: number;
  objectName: string;
  instant: Data;

  constructor() {
  }
}
