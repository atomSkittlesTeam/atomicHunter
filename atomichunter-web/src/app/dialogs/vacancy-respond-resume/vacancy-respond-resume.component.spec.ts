import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VacancyRespondResumeComponent } from './vacancy-respond-resume.component';

describe('VacancyRespondResumeComponent', () => {
  let component: VacancyRespondResumeComponent;
  let fixture: ComponentFixture<VacancyRespondResumeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VacancyRespondResumeComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VacancyRespondResumeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
