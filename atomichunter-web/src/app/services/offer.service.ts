import { Injectable } from '@angular/core';
import {BaseService} from "./base.service";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {ConfigService} from "../config/config.service";
import {firstValueFrom} from "rxjs";
import {Position} from "../dto/Position";
import {VacancyWithVacancyRespond} from "../dto/VacancyWithVacancyRespond";

@Injectable({
  providedIn: 'root'
})
export class OfferService extends BaseService {

  constructor(private http: HttpClient,
              public router: Router,
              public override configService: ConfigService) {
    super(configService);
  }

  async sendOffer(vacancyWithVacancyRespond: VacancyWithVacancyRespond) {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.post(url + '/offer/approve', vacancyWithVacancyRespond));
  }

  async sendDeclineOffer(vacancyWithVacancyRespond: VacancyWithVacancyRespond) {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.post(url + '/offer/decline', vacancyWithVacancyRespond));
  }
}
