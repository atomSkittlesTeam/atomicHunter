import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StaffUnitComponent } from './staff-unit.component';

describe('StaffUnitComponent', () => {
  let component: StaffUnitComponent;
  let fixture: ComponentFixture<StaffUnitComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StaffUnitComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StaffUnitComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
