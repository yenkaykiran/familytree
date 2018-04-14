import { Component, OnInit, ContentChild } from '@angular/core';

import { MemberListComponent } from './member-list/member-list.component';

import { Router } from '@angular/router';
import { Location } from '@angular/common';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'Yenkay Family Tree App';

  constructor(private route: Router, private location: Location) { }

  isIn = false;
  name: string;

  toggleState() { // click handler
      let bool = this.isIn;
      this.isIn = bool === false ? true : false;
  }

  ngOnInit() {

  }

  getAllByName(name) {
    var current = this.location.path();
    if(current) {
      if(!current.startsWith('/list') && !current.startsWith('/cyto-render')) {
        current = "/list";
      } else if (current.startsWith('/list')){
        current = '/list'
      } else if (current.startsWith('/cyto-render')){
        current = '/cyto-render'
      }
    }
    if(!name) {
      name = '';
    }
    this.route.navigate([current + '/' + name]);
  }
}
