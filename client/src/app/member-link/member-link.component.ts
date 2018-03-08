import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

import { Member, MemberHolder } from '../model/member';
import { MemberService } from '../member/member.service';

import { Http, Response } from '@angular/http';

@Component({
  selector: 'app-member-link',
  templateUrl: './member-link.component.html',
  styleUrls: ['./member-link.component.css']
})
export class MemberLinkComponent implements OnInit {

  constructor(private service: MemberService) { }

  @Input('member') member: Member;
  @Input('relation') relation: string;
  @Output('linked') linked = new EventEmitter();

  membersHolder: MemberHolder;
  members: Member[];

  ngOnInit() {
  }

  getAllByName(name) {
    if(name) {
      this.service.getAllByName(name).subscribe((res: MemberHolder) => {
        this.membersHolder = res;
        this.members = res.members;
      });
    }
  }

  link(selected) {
    this.service.linkMembers(selected.id, this.member.id, this.relation).subscribe((res: Response) => {
      this.linked.emit();
    });
  }
}
