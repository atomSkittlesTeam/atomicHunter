import {Component, OnInit, ViewChild} from '@angular/core';
import {StaffUnitDto} from "../../dto/StaffUnitDto";
import {Vacancy} from "../../dto/Vacancy";
import {Position} from "../../dto/Position";
import {CellClickedEvent, ColDef} from "ag-grid-community";
import {LoadingCellRendererComponent} from "../../platform/loading-cell-renderer/loading-cell-renderer.component";
import {AgGridAngular} from "ag-grid-angular";
import {OrgStructService} from "../../services/org-struct.service";
import {ActivatedRoute, Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {VacancyService} from "../../services/vacancy.service";
import {MessageService} from "primeng/api";
import {StatusEnum} from "../../dto/status-enum";

@Component({
  selector: 'app-vacancy-analysis',
  templateUrl: './vacancy-analysis.component.html',
  styleUrls: ['./vacancy-analysis.component.scss']
})
export class VacancyAnalysisComponent implements OnInit{

  loading: boolean = false;
  filter: boolean = false;
  openDialog: boolean = false;
  dialogEditMode: boolean = false;
  selectedStaff: StaffUnitDto;
  selectedVacancy: Vacancy;
  vacancyId: number;
  listFullNamesWithIdAndCheck: FullNameWithIdAndCheck[] = [];


  public columnDefs: ColDef[] = [
    {field: 'vacancyRespond.fullName', headerName: 'Кандидат', filter: 'agTextColumnFilter'},
    {field: 'competence.name', headerName: 'Критерий', filter: 'agTextColumnFilter'},
    {field: 'score', headerName: 'Оценка', filter: 'agTextColumnFilter'},
    {field: 'weight', headerName: 'Вес', filter: 'agTextColumnFilter'},
    {field: 'vacancyRespond.averageScore', headerName: 'Средняя оценка', filter: 'agTextColumnFilter'}
  ];

  public loadingCellRenderer: any = LoadingCellRendererComponent;
  public loadingCellRendererParams: any = {
    loadingMessage: 'Подождите еще немного...',
  };

  getEnum(type: string) {
    switch (type) {
      case 'Opened':
        return 'Открыто'
      case 'Closed':
        return 'Закрыто'
      case 'Pending':
        return 'Ожидание'
      default:
        return '';
    }
  }

  // DefaultColDef sets props common to all Columns
  public defaultColDef: ColDef = {
    editable: false,
    sortable: true,
    flex: 1,
    minWidth: 100,
    filter: true,
    resizable: true,
    floatingFilter: this.filter,
  };
  public rowData!: any[];

  @ViewChild(AgGridAngular) agGrid!: AgGridAngular;
  public overlayLoadingTemplate =
      '<span class="ag-overlay-loading-center">Скоро все появится, подождите еще немного...</span>';


  constructor(public orgStructService: OrgStructService,
              public router: Router,
              public http: HttpClient,
              private vacancyService: VacancyService,
              private messageService: MessageService,
              private activatedRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.activatedRoute.paramMap.subscribe(params => {
      let id = params.get('id');
      if (id != null) {
        this.vacancyId = Number.parseInt(id);
      }
    });
  }

  async onGridReady(grid: any) {
    this.agGrid = grid;
    await this.getAllVacancyAnalysisFromApi();
  }

  // Example of consuming Grid Event
  onCellClicked(e: CellClickedEvent): void {
    this.selectedStaff = e.data;
    console.log(this.selectedStaff);
    if (this.selectedStaff?.status === StatusEnum.Opened) {
      this.selectedVacancy = new Vacancy();
    }
  }

  showFilter() {
    this.filter = !this.filter;
    const columnDefs = this.agGrid.api.getColumnDefs();
    if (columnDefs) {
      columnDefs.forEach((colDef: any, index: number) => {
        colDef.floatingFilter = this.filter;
      });
      this.agGrid.api.setColumnDefs(columnDefs);
      this.agGrid.api.refreshHeader();
    }
  }

  async getAllVacancyAnalysisFromApi() {
    FullNameWithIdAndCheck
    this.agGrid.api.showLoadingOverlay();
    this.rowData = await this.vacancyService.getVacancyAnalysisByVacancyId(this.vacancyId);
    this.rowData.forEach(e => {
      this.listFullNamesWithIdAndCheck.push(new FullNameWithIdAndCheck(e.vacancyRespond.fullName,
          e.vacancyRespond.id, false));
    })
    this.loading = false;
  }
}

export class FullNameWithIdAndCheck {
  fullName: string;
  id: number;
  check: boolean = false;

  constructor(fullName: string, id: number, check: boolean) {
    this.fullName = fullName;
    this.id = id;
    this.check = check;
  }
}
