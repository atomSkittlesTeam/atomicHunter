import { Injectable } from '@angular/core';
import { BaseService } from './base.service';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { ConfigService } from '../config/config.service';
import { firstValueFrom } from 'rxjs';
import { Vacancy } from '../dto/Vacancy';

@Injectable({
  providedIn: 'root'
})
export class VacancyService extends BaseService {

  constructor(private http: HttpClient,
    public router: Router,
    public override configService: ConfigService) {
    super(configService);
  }

  async getVacancies(showArchive: boolean) {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.get<Vacancy[]>(url + '/vacancy/all',
        {
            params: {
                showArchive: showArchive
            }
        }
    ));
  }
  
  async archiveVacancy(id: number) {
    const url = await this.getBackendUrl();
    await firstValueFrom(this.http.delete(url + `/vacancy/${id}/archive`));
  }
}