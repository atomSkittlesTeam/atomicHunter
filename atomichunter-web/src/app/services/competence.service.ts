import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Router } from "@angular/router";
import { ConfigService } from "../config/config.service";
import { firstValueFrom } from "rxjs";
import { BaseService } from "./base.service";
import { Competence } from "../dto/Competence";

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
    return await firstValueFrom(this.http.get<Competence[]>(url + `/competence/position/${positionId}`));
  }

  async getAllCompetences() {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.get<Competence[]>(url + `/competence/all`));
  }
}
