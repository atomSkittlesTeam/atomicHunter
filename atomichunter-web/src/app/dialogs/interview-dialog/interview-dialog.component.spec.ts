import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VacancyRequestComponent } from './interview-dialog.component';

describe('VacancyRequestComponent', () => {
  let component: VacancyRequestComponent;
  let fixture: ComponentFixture<VacancyRequestComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VacancyRequestComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VacancyRequestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
