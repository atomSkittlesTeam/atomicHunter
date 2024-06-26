import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginFormComponent} from "./login-form/login-form.component";
import {NavigationComponent} from "./navigation/navigation.component";
import {AdminComponent} from "./admin/admin.component";
import {AuthGuard} from "./config/auth.guard";
import {AnalyticsComponent} from "./tables/analytics/analytics.component";
import {MasterDetailDetailComponent} from "./tables/master-detail-detail/master-detail-detail.component";
import {RegistrationFormComponent} from "./registration-form/registration-form.component";
import {VacancyComponent} from './tables/vacancy/vacancy.component';
import {VacancyRequestComponent} from "./dialogs/interview-dialog/interview-dialog.component";
import {PasswordRecoverFormComponent} from "./password-recover-form/password-recover-form.component";
import {StaffUnitComponent} from "./tables/staff-unit/staff-unit.component";
import {EmployeesComponent} from "./tables/employees/employees.component";
import {PositionsComponent} from "./tables/positions/positions.component";
import {CompetenceGroupComponent} from "./tables/competence-group/competence-group.component";
import {PlaceComponent} from "./tables/place/place.component";
import {CalendarComponent} from "./tables/calendar/calendar.component";
import {VacancyAnalysisComponent} from "./tables/vacancy-analysis/vacancy-analysis.component";

const routes: Routes = [
    {path: '', component: NavigationComponent, canActivate: [AuthGuard]},
    {path: 'login', component: LoginFormComponent},
    {path: 'registration', component: RegistrationFormComponent},
    {path: 'password-recover', component: PasswordRecoverFormComponent},
    {path: 'admin', component: AdminComponent,canActivate: [AuthGuard]},
    {path: 'vacancy', component: VacancyComponent,canActivate: [AuthGuard]},
    {path: 'calendar', component: CalendarComponent,canActivate: [AuthGuard]},
    {path: 'staff-unit', component: StaffUnitComponent,canActivate: [AuthGuard]},
    {path: 'employees', component: EmployeesComponent,canActivate: [AuthGuard]},
    {path: 'positions', component: PositionsComponent,canActivate: [AuthGuard]},
    {path: 'places', component: PlaceComponent,canActivate: [AuthGuard]},
    {path: 'competenceGroup', component: CompetenceGroupComponent, canActivate: [AuthGuard]},
    {path: 'analytics', component: AnalyticsComponent},
    {path: '3ple', component: MasterDetailDetailComponent},
    {path: 'vacancyRequest', component: VacancyRequestComponent},
    {path: 'vacancyAnalysis/:id', component: VacancyAnalysisComponent}
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
