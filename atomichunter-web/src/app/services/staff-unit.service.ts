import { Injectable } from '@angular/core';
import {BaseService} from "./base.service";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {ConfigService} from "../config/config.service";
import {firstValueFrom} from "rxjs";
import {Vacancy} from "../dto/Vacancy";
import {StaffUnitDto} from "../dto/StaffUnitDto";

@Injectable({
  providedIn: 'root'
})
export class StaffUnitService extends BaseService {

  constructor(private http: HttpClient,
              public router: Router,
              public override configService: ConfigService) {
    super(configService);
  }

  async getStaffUnits() {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.get<StaffUnitDto[]>(url + '/user/staff'));
  }
}
