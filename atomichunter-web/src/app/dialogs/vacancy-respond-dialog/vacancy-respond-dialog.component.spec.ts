import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VacancyRespondDialogComponent } from './vacancy-respond-dialog.component';

describe('VacancyRespondDialogComponent', () => {
  let component: VacancyRespondDialogComponent;
  let fixture: ComponentFixture<VacancyRespondDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VacancyRespondDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VacancyRespondDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
