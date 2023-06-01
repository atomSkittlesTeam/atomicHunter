import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VacancyRespondComponent } from './vacancy-respond.component';

describe('VacancyRespondComponent', () => {
  let component: VacancyRespondComponent;
  let fixture: ComponentFixture<VacancyRespondComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VacancyRespondComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VacancyRespondComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
