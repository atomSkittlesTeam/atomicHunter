import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {ConfigService} from "../config/config.service";
import {firstValueFrom} from "rxjs";
import {BaseService} from "./base.service";
import {Competence} from "../dto/Competence";
import {Vacancy} from "../dto/Vacancy";
import {CompetenceGroupDto} from "../dto/CompetenceGroupDto";
import {CompetenceGroupsWithCompetencesDto} from "../dto/CompetenceGroupsWithCompetencesDto";
import {CompetenceWeightScore} from "../dto/CompetenceWeightScore";
import {Employee} from "../dto/Employee";

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

    async getAllCompetenceGroups() {
        const url = await this.getBackendUrl();
        return await firstValueFrom(this.http.get<Competence[]>(url + `/competence/group/all`));
    }

    async getAllCompetenceTree() {
        const url = await this.getBackendUrl();
        return await firstValueFrom(this.http.get<CompetenceGroupsWithCompetencesDto[]>(url + `/competence/allTree`));
    }

    async getCompetencesByCompetenceGroupId(competenceGroupId: number) {
        const url = await this.getBackendUrl();
        return await firstValueFrom(this.http.get<Competence[]>(url + `/competence/group/${competenceGroupId}`));
    }

    async getCompetencesWeightScoreById(vacancyRespondId: number) {
        const url = await this.getBackendUrl();
        return await firstValueFrom(this.http.get<CompetenceWeightScore[]>(url + `/competence/${vacancyRespondId}`));
    }

    async getEmployeesWithScoreForRespond(vacancyRespondId: number) {
        const url = await this.getBackendUrl();
        return await firstValueFrom(this.http.get<Employee[]>(url + `/competence/${vacancyRespondId}/employees`));
    }

    async createCompetenceGroup(competenceGroup: CompetenceGroupDto) {
        const url = await this.getBackendUrl();
        return await firstValueFrom(this.http.post(url + '/competence/group', competenceGroup));
    }

    async updateCompetenceGroup(id: number, competenceGroup: CompetenceGroupDto) {
        const url = await this.getBackendUrl();
        return await firstValueFrom(this.http.put(url + `/competence/group/${id}/update`, competenceGroup));
    }

    async createCompetence(groupId: number, competence: Competence) {
        const url = await this.getBackendUrl();
        return await firstValueFrom(this.http.post(url + `/competence/group/${groupId}`, competence));
    }

    async updateCompetence(id: number, competence: Competence) {
        const url = await this.getBackendUrl();
        return await firstValueFrom(this.http.put(url + `/competence/${id}`, competence));
    }
}
