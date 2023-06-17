import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VacancyCompetenceScoreDialogComponent } from './vacancy-competence-score-dialog.component';

describe('VacancyCompetenceScoreDialogComponent', () => {
  let component: VacancyCompetenceScoreDialogComponent;
  let fixture: ComponentFixture<VacancyCompetenceScoreDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VacancyCompetenceScoreDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VacancyCompetenceScoreDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
