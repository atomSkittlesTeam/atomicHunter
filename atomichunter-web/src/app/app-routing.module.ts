import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginFormComponent} from "./login-form/login-form.component";
import {NavigationComponent} from "./navigation/navigation.component";
import {AdminComponent} from "./admin/admin.component";
import {RequestComponent} from "./tables/request/request.component";
import {AuthGuard} from "./config/auth.guard";
import { AnalyticsComponent } from "./tables/analytics/analytics.component";
import {MasterDetailDetailComponent} from "./tables/master-detail-detail/master-detail-detail.component";
import {TreeDataComponent} from "./tables/tree-data/tree-data.component";
import {RegistrationFormComponent} from "./registration-form/registration-form.component";
import { VacancyComponent } from './tables/vacancy/vacancy.component';
import {VacancyRequest} from "./dto/VacancyRequest";
import {VacancyRequestComponent} from "./dialogs/vacancy-request/vacancy-request.component";

const routes: Routes = [
    {path: '', component: NavigationComponent, canActivate: [AuthGuard]},
    {path: 'login', component: LoginFormComponent},
    {path: 'registration', component: RegistrationFormComponent},
    {path: 'admin', component: AdminComponent},
    {path: 'vacancy', component: VacancyComponent},
    {path: 'request', component: RequestComponent},
    {path: 'analytics', component: AnalyticsComponent},
    {path: '3ple', component: MasterDetailDetailComponent},
    {path: 'treeData', component: TreeDataComponent},
    {path: 'vacancyRequest', component: VacancyRequestComponent}
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
