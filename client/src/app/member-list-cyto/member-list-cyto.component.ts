import { Component, OnInit, ViewChild } from '@angular/core';
import { Response } from '@angular/http';
import { MemberService } from '../member/member.service';
import { Member, MemberHolder } from '../model/member';

import { NgbModal, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';
import { MemberEditComponent } from '../member-edit/member-edit.component';
import { MemberLinkComponent } from '../member-link/member-link.component';

import { ActivatedRoute, ParamMap } from '@angular/router';

import { CytoscapeComponent } from '../cytoscape/cytoscape.component';

@Component({
  selector: 'app-member-list-cyto',
  templateUrl: './member-list-cyto.component.html',
  styleUrls: ['./member-list-cyto.component.css']
})
export class MemberListCytoComponent implements OnInit {

  constructor(private service: MemberService, private modalService: NgbModal, private route: ActivatedRoute) { }

  @ViewChild("graph")
  graph: CytoscapeComponent;

  members: Member[];
  membersHolder: MemberHolder;
  page = 0;
  previousPage = 0;
  numbers;
  parent: Member;
  title: string;
  event: any;

  graphData = {
          nodes: [
              { data: { id: 'a', name: 'Signup', weight: 100, colorCode: 'blue', shapeType: 'roundrectangle' } },
              { data: { id: 'b', name: 'User Profile', weight: 100, colorCode: 'magenta', shapeType: 'roundrectangle' } },
              { data: { id: 'c', name: 'Billing', weight: 100, colorCode: 'magenta', shapeType: 'roundrectangle' } },
              { data: { id: 'd', name: 'Sales', weight: 100, colorCode: 'orange', shapeType: 'roundrectangle' } },
              { data: { id: 'e', name: 'Referral', weight: 100, colorCode: 'orange', shapeType: 'roundrectangle' } },
              { data: { id: 'f', name: 'Loan', weight: 100, colorCode: 'orange', shapeType: 'roundrectangle' } },
              { data: { id: 'j', name: 'Support', weight: 100, colorCode: 'red', shapeType: 'ellipse' } },
              { data: { id: 'k', name: 'Sink Event', weight: 100, colorCode: 'green', shapeType: 'ellipse' } }
          ],
          edges: [
              { data: { source: 'a', target: 'b', colorCode: 'blue', strength: 10 } },
              { data: { source: 'b', target: 'c', colorCode: 'blue', strength: 10 } },
              { data: { source: 'c', target: 'd', colorCode: 'blue', strength: 10 } },
              { data: { source: 'c', target: 'e', colorCode: 'blue', strength: 10 } },
              { data: { source: 'c', target: 'f', colorCode: 'blue', strength: 10 } },
              { data: { source: 'e', target: 'j', colorCode: 'red', strength: 10 } },
              { data: { source: 'e', target: 'k', colorCode: 'green', strength: 10 } }
          ]
  };

  ngOnInit() {
    this.route.paramMap.subscribe((params: ParamMap) => {
      let name = params.get('name');
      if(name) {
        this.getAllByName(name);
      }
    });
  }

  getAllByName(name) {
    this.parent = null;
    if(name) {
      this.service.getAllByName(name).subscribe((res: MemberHolder) => {
        this.membersHolder = res;
        this.members = res.members;
      });
    }
  }

  nodeChange(event) {
    console.log(event);
  }

}
