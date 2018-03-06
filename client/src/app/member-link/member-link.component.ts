import { Component, OnInit, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { Member, MemberHolder } from '../model/member';
import { MemberService } from '../member/member.service';

import { Http, Response } from '@angular/http';

@Component({
  selector: 'app-member-link',
  templateUrl: './member-link.component.html',
  styleUrls: ['./member-link.component.css']
})
export class MemberLinkComponent implements OnInit {

  constructor(private activeModal: NgbActiveModal, private service: MemberService) { }

  @Input('member') member: Member;
  relation = 'son';
  membersHolder: MemberHolder;
  members: Member[];
  selected: Member;

  ngOnInit() {
  }

  getRelation() {
    let title = 'Son';
    switch(this.relation) {
      case 'son':
        title = 'Son';
        break;
      case 'daughter':
        title = 'Daughter';
        break;
      case 'spouse':
        title = 'Spouse';
        break;
    }
    return title;
  }

  getAllByName(name) {
    if(name) {
      this.service.getAllByName(name).subscribe((res: MemberHolder) => {
        this.membersHolder = res;
        this.members = res.members;
        this.selected = this.members[0];
      });
    }
  }

  link() {
    this.service.linkMembers(this.selected.id, this.member.id, this.relation).subscribe((res: Response) => {
      this.activeModal.close();
    });
  }
}
