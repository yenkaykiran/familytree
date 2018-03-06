import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MemberLinkComponent } from './member-link.component';

describe('MemberLinkComponent', () => {
  let component: MemberLinkComponent;
  let fixture: ComponentFixture<MemberLinkComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MemberLinkComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MemberLinkComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
