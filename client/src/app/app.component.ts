import { Component, OnInit, ViewChild } from '@angular/core';

import { MemberListComponent } from './member-list/member-list.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'Yenkay Family Tree App';

  @ViewChild("membersList") membersList: MemberListComponent;

  constructor() { }

  isIn = false;
  name: string;

  toggleState() { // click handler
      let bool = this.isIn;
      this.isIn = bool === false ? true : false;
  }

  ngOnInit() {

  }
}
