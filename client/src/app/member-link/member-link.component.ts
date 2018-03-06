import { Component, OnInit, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { Member, MemberHolder } from '../model/member';
import { MemberService } from '../member/member.service';

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
    let relationList = this.member[this.relation];
    if(!relationList) {
      relationList = new Array<Member>();
    }
    relationList.push(this.selected);
    console.log(this.member);
    this.service.save(this.member).subscribe((res: Member) => {
      switch(this.relation) {
        case 'son':
          this.selected.father = res;
          break;
        case 'daughter':
          this.selected.father = res;
          break;
        case 'spouse':
          let relationList1 = this.selected['spouse'];
          if(!relationList1) {
            relationList1 = new Array<Member>();
          }
          relationList1.push(res);
          break;
      }
      this.service.save(this.selected).subscribe((res1: Member) => {
        this.activeModal.close(res);
      });
    });
  }
}
