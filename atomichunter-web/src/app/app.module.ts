import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {LoginFormComponent} from './login-form/login-form.component';
import {MessageModule} from "primeng/message";
import {MessagesModule} from "primeng/messages";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {ButtonModule} from "primeng/button";
import {TableModule} from "primeng/table";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {NavigationComponent} from './navigation/navigation.component';
import {AdminComponent} from './admin/admin.component';
import {ToastModule} from "primeng/toast";
import {DialogModule} from "primeng/dialog";
import {SelectButtonModule} from "primeng/selectbutton";
import {ContextMenuModule} from "primeng/contextmenu";
import {ConfigService} from "./config/config.service";
import {MultiSelectModule} from "primeng/multiselect";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {SpeedDialModule} from "primeng/speeddial";
import {SplitterModule} from "primeng/splitter";
import {SidebarModule} from 'primeng/sidebar'
import {ToolbarModule} from "primeng/toolbar";
import {InputSwitchModule} from "primeng/inputswitch";
import {BlockUIModule} from "primeng/blockui";
import {CheckboxModule} from "primeng/checkbox";
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {AnalyticsComponent} from './tables/analytics/analytics.component';
import {ChartModule} from "primeng/chart";
import {AuthGuard} from "./config/auth.guard";
import {AgGridModule} from "ag-grid-angular";
import {DialogComponent} from './platform/dialog/dialog.component';
import {DialogRowComponent} from './platform/dialog-row/dialog-row.component';
import {CalendarModule} from "primeng/calendar";
import {ChipsModule} from "primeng/chips";
import {InputTextareaModule} from "primeng/inputtextarea";
import { ConfirmationService, MessageService } from 'primeng/api';
import { DropdownModule } from 'primeng/dropdown';
import { MasterDetailDetailComponent } from './tables/master-detail-detail/master-detail-detail.component';
import { AdminDialogComponent } from './dialogs/admin-dialog/admin-dialog.component';
import { HttpHelperInterceptor } from './interceptors/http-helper.interceptor';
import {LoadingCellRendererComponent} from './platform/loading-cell-renderer/loading-cell-renderer.component';
import { TreeDataComponent } from './tables/tree-data/tree-data.component';
import {RowGroupingModule} from "@ag-grid-enterprise/row-grouping";
import { RegistrationFormComponent } from './registration-form/registration-form.component';
import { UserDialogComponent } from './dialogs/user-dialog/user-dialog.component';
import { PasswordModule } from "primeng/password";
import {NgOptimizedImage} from "@angular/common";
import { VacancyComponent } from './tables/vacancy/vacancy.component';
import { VacancyDialogComponent } from './dialogs/vacancy-dialog/vacancy-dialog.component';
import { RadioButtonModule } from "primeng/radiobutton";
import { KnobModule } from "primeng/knob";
import { InputNumberModule } from "primeng/inputnumber";
import { VacancyRespondComponent } from './tables/vacancy-respond/vacancy-respond.component';
import { VacancyRequestComponent } from './dialogs/interview-dialog/interview-dialog.component';
import {SplitButtonModule} from "primeng/splitbutton";
import { PasswordRecoverFormComponent } from './password-recover-form/password-recover-form.component';
import { MenubarModule } from "primeng/menubar";
import { MenuModule } from "primeng/menu";
import { StaffUnitComponent } from './tables/staff-unit/staff-unit.component';
import { EmployeesComponent } from './tables/employees/employees.component';
import { PositionsComponent } from './tables/positions/positions.component';
import { CompetenceGroupComponent } from './tables/competence-group/competence-group.component';
import { CompetenceComponent } from './tables/competence/competence.component';
import { CompetenceGroupDialogComponent } from './dialogs/competence-group-dialog/competence-group-dialog.component';
import { CompetenceDialogComponent } from './dialogs/competence-dialog/competence-dialog.component';
import { VacancyRespondDialogComponent } from './dialogs/vacancy-respond-dialog/vacancy-respond-dialog.component';

@NgModule({
    declarations: [
        AppComponent,
        LoginFormComponent,
        NavigationComponent,
        AdminComponent,
        AnalyticsComponent,
        DialogComponent,
        DialogRowComponent,
        MasterDetailDetailComponent,
        AdminDialogComponent,
        LoadingCellRendererComponent,
        TreeDataComponent,
        RegistrationFormComponent,
        UserDialogComponent,
        VacancyComponent,
        VacancyDialogComponent,
        VacancyRespondComponent,
        VacancyRequestComponent,
        PasswordRecoverFormComponent,
        StaffUnitComponent,
        EmployeesComponent,
        PositionsComponent,
        CompetenceGroupComponent,
        CompetenceComponent,
        CompetenceGroupDialogComponent,
        CompetenceDialogComponent,
        VacancyRespondDialogComponent
    ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    MessageModule,
    MessagesModule,
    FormsModule,
    ButtonModule,
    TableModule,
    ToastModule,
    DialogModule,
    SelectButtonModule,
    ContextMenuModule,
    BrowserAnimationsModule,
    MultiSelectModule,
    SpeedDialModule,
    SplitterModule,
    SidebarModule,
    ToolbarModule,
    InputSwitchModule,
    BlockUIModule,
    CheckboxModule,
    ConfirmDialogModule,
    ChartModule,
    AgGridModule,
    CalendarModule,
    ChipsModule,
    InputTextareaModule,
    ReactiveFormsModule,
    DropdownModule,
    PasswordModule,
    NgOptimizedImage,
    RadioButtonModule,
    KnobModule,
    InputNumberModule,
    SplitButtonModule,
    MenubarModule,
    MenuModule
  ],
    providers: 
    [ConfigService,
    AuthGuard,
    MessageService,
    ConfirmationService,
    {
        provide: HTTP_INTERCEPTORS,
        useClass: HttpHelperInterceptor,
        multi: true,
    }],
    bootstrap: [AppComponent]
})
export class AppModule {
}
