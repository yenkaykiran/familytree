import { Component, OnInit } from '@angular/core';
import { Response } from '@angular/http';
import { MemberService } from '../member/member.service';
import { Member, MemberHolder } from '../model/member';

import { NgbModal, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';
import { MemberEditComponent } from '../member-edit/member-edit.component';
import { MemberLinkComponent } from '../member-link/member-link.component';

@Component({
  selector: 'app-member-list',
  templateUrl: './member-list.component.html',
  styleUrls: ['./member-list.component.css']
})
export class MemberListComponent implements OnInit {

  constructor(private service: MemberService, private modalService: NgbModal) { }

  members: Member[];
  membersHolder: MemberHolder;
  page = 0;
  previousPage = 0;
  numbers;
  parent: Member;
  title: string;

  ngOnInit() {
    this.getAll();
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

  link() {
    if(this.parent) {
      const modalRef = this.modalService.open(MemberLinkComponent, { size: 'lg', backdrop: 'static'});
      modalRef.componentInstance.member = this.parent;
      modalRef.result.then((result) => {
        
      }, (result) => {

      });
    }
  }

  fetchRelated($event, member: Member) {
    this.parent = member;
    this.title = $event.title;
    this.service.fetchRelated($event.relation, member).subscribe((res: MemberHolder) => {
      member.son = res.members;
    });
  }

  preparePages() {
    this.numbers = Array.from(Array(this.membersHolder.totalPages)).map((x,i)=>i);
  }
}
