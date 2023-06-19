import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VacancyAnalysisComponent } from './vacancy-analysis.component';

describe('VacancyAnalysisComponent', () => {
  let component: VacancyAnalysisComponent;
  let fixture: ComponentFixture<VacancyAnalysisComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VacancyAnalysisComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VacancyAnalysisComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
