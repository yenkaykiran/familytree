import { Component, OnInit } from '@angular/core';
import { Response } from '@angular/http';
import { MemberService } from '../member/member.service';
import { Member, MemberHolder } from '../model/member';
import { ActivatedRoute, ParamMap } from '@angular/router';

import { NgbModal, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';
import { MemberEditComponent } from '../member-edit/member-edit.component';
import { MemberLinkComponent } from '../member-link/member-link.component';

@Component({
  selector: 'app-member-list',
  templateUrl: './member-list.component.html',
  styleUrls: ['./member-list.component.css']
})
export class MemberListComponent implements OnInit {

  constructor(private service: MemberService, private modalService: NgbModal, private route: ActivatedRoute) { }

  members: Member[];
  membersHolder: MemberHolder;
  page = 0;
  previousPage = 0;
  numbers;
  parent: Member;
  title: string;
  event: any;

  ngOnInit() {
    this.route.paramMap.subscribe((params: ParamMap) => {
      let name = params.get('name');
      if(name) {
        this.getAllByName(name);
      } else {
        this.getAll();
      }
    });
  }

  getAll() {
    this.parent = null;
    this.service.getAll(this.page).subscribe((res: MemberHolder) => {
      this.membersHolder = res;
      this.members = res.members;
      this.preparePages();
    });
  }

  getAllByName(name) {
    this.parent = null;
    if(name) {
      this.service.getAllByName(name).subscribe((res: MemberHolder) => {
        this.membersHolder = res;
        this.members = res.members;
        this.preparePages();
      });
    } else {
      this.getAll();
    }
  }

  loadPage(p: number) {
    if (p !== this.previousPage) {
      this.previousPage = p;
      this.page = p;
      this.getAll();
    }
  }

  add() {
    const modalRef = this.modalService.open(MemberEditComponent, { size: 'lg', backdrop: 'static'});
    modalRef.componentInstance.member = new Member();
    modalRef.result.then((result) => {
      this.getAll();
    }, (result) => {
      this.getAll();
    });
  }

  fetchRelatedAgain(member: Member) {
    this.fetchRelated(this.event, member);
  }

  fetchRelated($event, member: Member) {
    this.parent = member;
    this.title = $event.title;
    this.event = $event;
    member[$event.relation] = null;
    this.service.fetchRelated($event.relation, member).subscribe((res: MemberHolder) => {
      member[$event.relation] = res.members;
    });
  }

  preparePages() {
    this.numbers = Array.from(Array(this.membersHolder.totalPages)).map((x,i)=>i);
  }
}
