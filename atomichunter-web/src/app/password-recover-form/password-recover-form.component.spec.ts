import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PasswordRecoverFormComponent } from './password-recover-form.component';

describe('PasswordRecoverFormComponent', () => {
  let component: PasswordRecoverFormComponent;
  let fixture: ComponentFixture<PasswordRecoverFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PasswordRecoverFormComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PasswordRecoverFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
