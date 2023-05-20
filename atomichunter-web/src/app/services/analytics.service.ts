import { Injectable } from '@angular/core';
import { firstValueFrom } from "rxjs";
import { BaseService } from "./base.service";
import { HttpClient } from "@angular/common/http";
import { Router } from "@angular/router";
import { ConfigService } from "../config/config.service";

@Injectable({
  providedIn: 'root'
})
export class AnalyticsService extends BaseService {

  constructor(private http: HttpClient,
              public router: Router,
              public override configService: ConfigService) {
    super(configService);
  }

  async getAnalyticsPositionCountByNumber() {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.get<Map<string, number>>(url + `/analytics/request-position-count-by-request-number`, {}));
  }

  // async getAnalyticsAllMachines() {
  //   const url = await this.getBackendUrl();
  //   return await firstValueFrom(this.http.get<Map<string, number>>(url + `/analytics-all`, {}));
  // }

}
