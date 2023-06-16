import { TestBed } from '@angular/core/testing';

import { StaffUnitService } from './staff-unit.service';

describe('StaffUnitService', () => {
  let service: StaffUnitService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StaffUnitService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
