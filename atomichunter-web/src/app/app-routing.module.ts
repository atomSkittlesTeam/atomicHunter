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
import {Interview} from "./dto/Interview";
import {VacancyRequestComponent} from "./dialogs/interview-dialog/interview-dialog.component";
import {PasswordRecoverFormComponent} from "./password-recover-form/password-recover-form.component";

const routes: Routes = [
    {path: '', component: NavigationComponent, canActivate: [AuthGuard]},
    {path: 'login', component: LoginFormComponent},
    {path: 'registration', component: RegistrationFormComponent},
    {path: 'password-recover', component: PasswordRecoverFormComponent},
    {path: 'admin', component: AdminComponent,canActivate: [AuthGuard]},
    {path: 'vacancy', component: VacancyComponent,canActivate: [AuthGuard]},
    {path: 'request', component: RequestComponent,canActivate: [AuthGuard]},
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
