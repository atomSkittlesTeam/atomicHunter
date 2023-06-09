import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { ConfigService } from '../config/config.service';
import { HttpClient } from '@angular/common/http';
import { BaseService } from './base.service';
import { firstValueFrom } from 'rxjs';
import { VacancyWithVacancyRespond } from "../dto/VacancyWithVacancyRespond";
import { Interview } from '../dto/Interview';

@Injectable({
  providedIn: 'root'
})
export class InviteService extends BaseService {

  constructor(private http: HttpClient,
    public router: Router,
    public override configService: ConfigService) {
    super(configService);
  }

  async inviteToInterview(vacancyRespondId: number, interview: Interview) {   
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.post(url + `/invite/interview/vacancy-respond/${vacancyRespondId}`, interview));
  }

  async getInterviewById(interviewId: number) {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.get<Interview>(url + `/invite/interview/${interviewId}`));
  }

  async sendOffer(vacancyWithVacancyRespond: VacancyWithVacancyRespond) {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.post(url + '/invite/offer', vacancyWithVacancyRespond));
  }
}
