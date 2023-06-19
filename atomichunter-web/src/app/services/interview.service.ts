import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { ConfigService } from '../config/config.service';
import { HttpClient } from '@angular/common/http';
import { BaseService } from './base.service';
import { firstValueFrom } from 'rxjs';
import { VacancyWithVacancyRespond } from "../dto/VacancyWithVacancyRespond";
import { Interview } from '../dto/Interview';
import {Calendar} from "primeng/calendar";
import {InterviewCalendarDto} from "../dto/InterviewCalendarDto";

@Injectable({
  providedIn: 'root'
})
export class InterviewService extends BaseService {

  constructor(private http: HttpClient,
    public router: Router,
    public override configService: ConfigService) {
    super(configService);
  }

  async createInterview(vacancyRespondId: number, interview: Interview) {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.post(url + `/interview/vacancy-respond/${vacancyRespondId}`, interview));
  }

  async getInterviewById(interviewId: number) {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.get<Interview>(url + `/interview/${interviewId}`));
  }

  async sendOffer(vacancyWithVacancyRespond: VacancyWithVacancyRespond) {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.post(url + '/invite/offer', vacancyWithVacancyRespond));
  }

  async updateInterview(interviewId: number, interview: Interview) {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.put<Interview>(url + `/interview/${interviewId}`, interview));
  }

  async validateInterview(interview: Interview) {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.post<string[]>(url + `/interview/validate`, interview));
  }
  async getCalendar(showArchive: boolean) {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.get<InterviewCalendarDto>(url + `/interview/calendar`,
    {
      params: {
          showArchive: showArchive
      }
    }));
  }
  async deleteInterview(interviewId: number) {
    const url = await this.getBackendUrl();
    await firstValueFrom(this.http.delete(url + `/interview/delete/${interviewId}`));
  }
}
