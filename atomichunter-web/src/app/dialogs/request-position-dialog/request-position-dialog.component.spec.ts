import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RequestPositionDialogComponent } from './request-position-dialog.component';

describe('RequestPositionDialogComponent', () => {
  let component: RequestPositionDialogComponent;
  let fixture: ComponentFixture<RequestPositionDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RequestPositionDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RequestPositionDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
