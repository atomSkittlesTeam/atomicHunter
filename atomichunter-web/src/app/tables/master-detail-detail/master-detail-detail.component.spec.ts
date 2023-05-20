import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MasterDetailDetailComponent } from './master-detail-detail.component';

describe('MasterDetailDetailComponent', () => {
  let component: MasterDetailDetailComponent;
  let fixture: ComponentFixture<MasterDetailDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MasterDetailDetailComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MasterDetailDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
