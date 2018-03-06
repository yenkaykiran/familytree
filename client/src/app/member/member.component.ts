import { Component, OnInit, Input, Output, ViewChild, EventEmitter } from '@angular/core';
import { Response } from '@angular/http';
import { MemberService } from './member.service';

import { Member } from '../model/member';

import { NgbModal, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';

import { MemberEditComponent } from '../member-edit/member-edit.component';

@Component({
  selector: 'app-member',
  templateUrl: './member.component.html',
  styleUrls: ['./member.component.css']
})
export class MemberComponent implements OnInit {

  constructor(private service: MemberService, private modalService: NgbModal) { }

  @Input('member') member: Member;
  @Output('fetchRelated') related = new EventEmitter<any>();

  ngOnInit() {

  }

  open() {
    const modalRef = this.modalService.open(MemberEditComponent, { size: 'lg', backdrop: 'static'});
    modalRef.componentInstance.member = JSON.parse(JSON.stringify(this.member));
    modalRef.result.then((result) => {
      this.member = result;
    }, (result) => { });
  }

  delete() {
    this.service.delete(this.member.id).subscribe((response) => {
      this.member = null;
    });
  }

  fetchRelated(rel: string, title: string) {
    this.related.emit({
      'relation': rel,
      'title': title
    });
  }
}
