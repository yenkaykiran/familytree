import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MemberUnlinkComponent } from './member-unlink.component';

describe('MemberUnlinkComponent', () => {
  let component: MemberUnlinkComponent;
  let fixture: ComponentFixture<MemberUnlinkComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MemberUnlinkComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MemberUnlinkComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
