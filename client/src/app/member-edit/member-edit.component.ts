import { Component, OnInit, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { Member } from '../model/member';
import { MemberService } from '../member/member.service';

@Component({
  selector: 'app-member-edit',
  templateUrl: './member-edit.component.html',
  styleUrls: ['./member-edit.component.css']
})
export class MemberEditComponent implements OnInit {

  constructor(private activeModal: NgbActiveModal, private service: MemberService) { }

  @Input('member') member: Member;

  ngOnInit() {
  }

  save() {
    this.service.save(this.member).subscribe((res: Member) => {
      this.activeModal.close(res);
    });
  }
}
