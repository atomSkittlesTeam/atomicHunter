import { Injectable } from '@angular/core';
import { BaseService } from './base.service';
import { Position } from '../dto/Position';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { ConfigService } from '../config/config.service';
import { firstValueFrom } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PositionService extends BaseService {

  constructor(private http: HttpClient,
              public router: Router,
              public override configService: ConfigService) {
    super(configService);
  }

  async getPositions() {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.get<Position[]>(url + '/positions/all'));
  }
}
