import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {ConfigService} from "../config/config.service";
import {firstValueFrom} from "rxjs";
import {StaffUnitDto} from "../dto/StaffUnitDto";
import {BaseService} from "./base.service";
import {Position} from "../dto/Position";
import {Employee} from "../dto/Employee";

@Injectable({
  providedIn: 'root'
})
export class OrgStructService extends BaseService {

  constructor(private http: HttpClient,
              public router: Router,
              public override configService: ConfigService) {
    super(configService);
  }

  async getStaffUnits() {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.get<StaffUnitDto[]>(url + '/org-struct/staff-units'));
  }

  async getEmployees() {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.get<Employee[]>(url + '/org-struct/employees'));
  }

  async getHrEmployees() {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.get<Employee[]>(url + '/org-struct/employees/hr'));
  }

  async getPositions() {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.get<Position[]>(url + '/org-struct/positions'));
  }
}
