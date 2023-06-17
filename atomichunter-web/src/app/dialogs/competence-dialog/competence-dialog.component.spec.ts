import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CompetenceDialogComponent } from './competence-dialog.component';

describe('CompetenceDialogComponent', () => {
  let component: CompetenceDialogComponent;
  let fixture: ComponentFixture<CompetenceDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CompetenceDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CompetenceDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
