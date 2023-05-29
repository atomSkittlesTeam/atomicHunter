import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Router } from "@angular/router";
import { ConfigService } from "../config/config.service";
import { firstValueFrom } from "rxjs";
import { Vacancy } from "../dto/Vacancy";
import { BaseService } from "./base.service";

@Injectable({
  providedIn: "root"
})
export class CompetenceService extends BaseService {

  constructor(private http: HttpClient,
              public router: Router,
              public override configService: ConfigService) {
    super(configService);
  }

  async getCompetencesByPositionId(positionId: number) {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.get<any[]>(url + `/competence/position/${positionId}`));
  }
}
