import {Data} from "@angular/router";

export class Request {
    id: number;
    number: string;
    requestDate: Data;
    priority: number;
    description: string;
    releaseDate: Data;
    archive: boolean;

    constructor() {
    }
}
