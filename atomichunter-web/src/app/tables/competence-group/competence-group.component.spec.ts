import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CompetenceGroupComponent } from './competence-group.component';

describe('CompetenceGroupComponent', () => {
  let component: CompetenceGroupComponent;
  let fixture: ComponentFixture<CompetenceGroupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CompetenceGroupComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CompetenceGroupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
