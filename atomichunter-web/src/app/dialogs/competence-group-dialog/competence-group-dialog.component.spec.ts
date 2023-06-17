import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CompetenceGroupDialogComponent } from './competence-group-dialog.component';

describe('CompetenceGroupDialogComponent', () => {
  let component: CompetenceGroupDialogComponent;
  let fixture: ComponentFixture<CompetenceGroupDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CompetenceGroupDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CompetenceGroupDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
