import {Injectable} from '@angular/core';
import {firstValueFrom} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {ConfigService} from "../config/config.service";
import {BaseService} from "./base.service";
import {Request} from "../dto/Request";
import { RequestPosition } from '../dto/RequestPosition';
import { Product } from '../dto/Product';

@Injectable({
    providedIn: 'root'
})
export class RequestService extends BaseService {

    constructor(private http: HttpClient,
                public router: Router,
                public override configService: ConfigService) {
        super(configService);
    }

    async getRequests(showArchive: boolean) {
        const url = await this.getBackendUrl();
        return await firstValueFrom(this.http.get<Request[]>(url + '/requests/all',
            {
                params: {
                    showArchive: showArchive
                }
            }
        ));
    }

    async getRequestPositions(requestId: number, showArchive: boolean) {
        const url = await this.getBackendUrl();
        return await firstValueFrom(this.http.get<RequestPosition[]>(url + `/requests/${requestId}/positions/all`,
            {
                params: {
                    showArchive: showArchive
                }
            }
        ));
    }

    async createRequest(request: Request) {
        const url = await this.getBackendUrl();
        return await firstValueFrom(this.http.post(url + '/requests/create', request));
    }

    async updateRequest(id: number, request: Request) {
        const url = await this.getBackendUrl();
        return await firstValueFrom(this.http.put(url + `/requests/${id}/update`, request));
    }

    async archiveRequest(id: number) {
        const url = await this.getBackendUrl();
        await firstValueFrom(this.http.delete(url + `/requests/${id}/archive`));
    }

    async createRequestPosition(requestPosition: RequestPosition) {
        const url = await this.getBackendUrl();
        return await firstValueFrom(this.http.post(url + '/requests/positions/create', requestPosition));
    }

    async updateRequestPosition(requestPositionId: number, requestPosition: RequestPosition) {
        const url = await this.getBackendUrl();
        return await firstValueFrom(this.http.put(url 
            + `/requests/positions/${requestPositionId}/update`, requestPosition));
    }

    async archiveRequestPosition(requestPositionId: number) {
        const url = await this.getBackendUrl();
        await firstValueFrom(this.http.delete(url + `/requests/positions/${requestPositionId}/archive`));
    }

    async getProducts() {
        const url = await this.getBackendUrl();
        return await firstValueFrom(this.http.get<Product[]>(url + '/products/all'));
    }
}
