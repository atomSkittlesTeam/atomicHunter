import {Injectable} from '@angular/core';
import {BaseService} from './base.service';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {ConfigService} from '../config/config.service';
import {firstValueFrom} from 'rxjs';
import {Vacancy} from '../dto/Vacancy';
import {VacancyRespond} from "../dto/VacancyRespond";
import {VacancyCompetenceScoreRequestDto} from "../dto/VacancyCompetenceScoreRequestDto";
import _default from "chart.js/dist/plugins/plugin.tooltip";
import numbers = _default.defaults.animations.numbers;
import {CompetenceWeightScoreFull} from "../dto/CompetenceWeightScoreFull";

@Injectable({
    providedIn: 'root'
})
export class VacancyService extends BaseService {

    constructor(private http: HttpClient,
                public router: Router,
                public override configService: ConfigService) {
        super(configService);
    }

    async getVacancies(showArchive: boolean, showClose: boolean) {
        const url = await this.getBackendUrl();
        return await firstValueFrom(this.http.get<Vacancy[]>(url + '/vacancy/all',
            {
                params: {
                    showArchive: showArchive,
                    showClose: showClose
                }
            }
        ));
    }

    async getVacancyById(id: number) {
        const url = await this.getBackendUrl();
        return await firstValueFrom(this.http.get<Vacancy>(url + `/vacancy/${id}`));
    }

    async createVacancy(vacancy: Vacancy) {
        const url = await this.getBackendUrl();
        return await firstValueFrom(this.http.post(url + '/vacancy/create', vacancy));
    }

    async updateVacancy(id: number, vacancy: Vacancy) {
        const url = await this.getBackendUrl();
        return await firstValueFrom(this.http.put(url + `/vacancy/${id}/update`, vacancy));
    }

    async archiveVacancy(id: number) {
        const url = await this.getBackendUrl();
        await firstValueFrom(this.http.delete(url + `/vacancy/${id}/archive`));
    }

    async closeVacancy(id: number, vacancyRespondId: number) {
        const url = await this.getBackendUrl();
        await firstValueFrom(this.http.delete(url + `/vacancy/${id}/${vacancyRespondId}/close`));
    }

    async getVacancyRespondsByIds(ids: number[], showArchive: boolean,) {
        const url = await this.getBackendUrl();
        return await firstValueFrom(this.http.post<VacancyRespond[]>(url + '/vacancy/respond/get-all-by-ids', ids,
            {
                params: {
                    showArchive: showArchive
                }
            }
        ));
    }

    async validateVacancyCompetenceScore(vacancyCompScoreReq: VacancyCompetenceScoreRequestDto) {
        const url = await this.getBackendUrl();
        return await firstValueFrom(this.http.post<VacancyCompetenceScoreRequestDto>(url + '/vacancy/competence-score/add', vacancyCompScoreReq));
    }

    async validateVacancyCompetenceScoreByEmployeeId(employeeId: string, vacancyPositionId: number) {
        const url = await this.getBackendUrl();
        return await firstValueFrom(this.http.get<boolean>(url + `/vacancy/competence-score/${employeeId}/${vacancyPositionId}/validation`));
    }

    async getVacancyRespondById(id: number) {
        const url = await this.getBackendUrl();
        return await firstValueFrom(this.http.get<VacancyRespond>(url + `/vacancy/respond/${id}`));
    }

    async getVacancyRespondWithInterviewById(id: number) {
        const url = await this.getBackendUrl();
        return await firstValueFrom(this.http.get<VacancyRespond>(url + `/vacancy/respond/${id}/interview`));
    }

    async createVacancyRespond(vacancyRespond: VacancyRespond) {
        const url = await this.getBackendUrl();
        return await firstValueFrom(this.http.post(url + '/vacancy/respond/create', vacancyRespond));
    }

    async updateVacancyRespond(vacancyRespondId: number, vacancyRespond: VacancyRespond) {
        const url = await this.getBackendUrl();
        return await firstValueFrom(this.http.put(url + `/vacancy/respond/${vacancyRespondId}/update`, vacancyRespond));
    }

    async archiveVacancyRespond(vacancyRespondId: number) {
        const url = await this.getBackendUrl();
        await firstValueFrom(this.http.delete(url + `/vacancy/respond/${vacancyRespondId}/archive`));
    }

    async createVacancyReport(vacancyId: number, additionalInformationForReport: string) {
        const url = await this.getBackendUrl();
        return await firstValueFrom(this.http.post<string[]>(
            url + `/vacancy/${vacancyId}/report`, additionalInformationForReport));
    }

  async getVacancyFileReport(vacancyId: number, path: string) {
    const url = await this.getBackendUrl();
    window.open(url + `/report/vacancy/${path}`, '_blank');
  }

  async getVacancyAnalysisByVacancyId(vacancyId: number, checkedIds: number[]) {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.post<CompetenceWeightScoreFull[]>(url + `/vacancy/${vacancyId}/analysis`, checkedIds));
  }
}
