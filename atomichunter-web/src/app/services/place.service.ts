import { Injectable } from '@angular/core';
import {BaseService} from "./base.service";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {ConfigService} from "../config/config.service";
import {firstValueFrom} from "rxjs";
import {Vacancy} from "../dto/Vacancy";
import {Place} from "../dto/Place";

@Injectable({
  providedIn: 'root'
})
export class PlaceService extends BaseService {

  constructor(private http: HttpClient,
              public router: Router,
              public override configService: ConfigService) {
    super(configService);
  }

  async getPlaces(showArchive: boolean) {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.get<Vacancy[]>(url + '/place/all',
        {
          params: {
            showArchive: showArchive
          }
        }
    ));
  }

  async getPlaceById(id: number) {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.get<Vacancy>(url + `/place/${id}`));
  }

  async createPlace(place: Place) {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.post(url + '/place/create', place));
  }

  async updatePlace(id: number, place: Place) {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.put(url + `/place/${id}/update`, place));
  }

  async archivePlace(id: number) {
    const url = await this.getBackendUrl();
    await firstValueFrom(this.http.delete(url + `/place/${id}/archive`));
  }
}
