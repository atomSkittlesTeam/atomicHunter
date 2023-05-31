import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { ConfigService } from '../config/config.service';
import { HttpClient } from '@angular/common/http';
import { BaseService } from './base.service';
import { VacancyRespond } from '../dto/VacancyRespond';
import { firstValueFrom } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class InviteService extends BaseService {

  constructor(private http: HttpClient,
    public router: Router,
    public override configService: ConfigService) {
    super(configService);
  }

  async inviteToInterview(vacancyRespond: VacancyRespond) {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.post(url + '/invite/interview', vacancyRespond));
  }

  async sendOffer(vacancyRespond: VacancyRespond) {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.post(url + '/invite/offer', vacancyRespond));
  }
}
