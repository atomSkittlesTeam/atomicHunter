import { TestBed } from '@angular/core/testing';

import { OrgStructService } from './org-struct.service';

describe('OrgStructService', () => {
  let service: OrgStructService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OrgStructService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
