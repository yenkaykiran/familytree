import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

import { Member, MemberHolder } from '../model/member';
import { MemberService } from '../member/member.service';

import { Http, Response } from '@angular/http';

@Component({
  selector: 'app-member-unlink',
  templateUrl: './member-unlink.component.html',
  styleUrls: ['./member-unlink.component.css']
})
export class MemberUnlinkComponent implements OnInit {

  constructor(private service: MemberService) { }

  @Input('member') member: Member;
  @Input('relation') relation: string;
  @Output('unlinked') unlinked = new EventEmitter();

  membersHolder: MemberHolder;
  members: Member[];

  ngOnInit() {
    // this.fetchRelated();
  }

  fetchRelated() {
    this.service.fetchRelated(this.relation, this.member).subscribe((res: MemberHolder) => {
      this.membersHolder = res;
      this.members = res.members;
    });
  }

  unlink(selected) {
    this.service.unlinkMembers(selected.id, this.member.id, this.relation).subscribe((res: Response) => {
      this.fetchRelated();
      this.unlinked.emit();
    });
  }
}
