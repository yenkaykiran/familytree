import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MemberListCytoComponent } from './member-list-cyto.component';

describe('MemberListCytoComponent', () => {
  let component: MemberListCytoComponent;
  let fixture: ComponentFixture<MemberListCytoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MemberListCytoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MemberListCytoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
