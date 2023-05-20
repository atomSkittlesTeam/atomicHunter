import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RequestPositionsComponent } from './request-positions.component';

describe('RequestPositionsComponent', () => {
  let component: RequestPositionsComponent;
  let fixture: ComponentFixture<RequestPositionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RequestPositionsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RequestPositionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
