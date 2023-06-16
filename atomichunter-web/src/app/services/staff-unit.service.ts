import { Injectable } from '@angular/core';
import {BaseService} from "./base.service";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {ConfigService} from "../config/config.service";

@Injectable({
  providedIn: 'root'
})
export class StaffUnitService extends BaseService {

  constructor(private http: HttpClient,
              public router: Router,
              public override configService: ConfigService) {
    super(configService);
  }
}
