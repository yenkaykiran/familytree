import { Component, OnInit, ContentChild } from '@angular/core';

import { MemberListComponent } from './member-list/member-list.component';

import { Router } from '@angular/router';
import { Location } from '@angular/common';

import { TokenService } from './services/token.service';

import {
  trigger,
  state,
  style,
  animate,
  transition
} from '@angular/animations';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  animations: [
  trigger('collapse', [
    state('open', style({
      opacity: '1',
      display: 'block',
      transform: 'translate3d(0, 0, 0)'
    })),
    state('closed',   style({
      opacity: '0',
      display: 'none',
      transform: 'translate3d(0, -100%, 0)'
    })),
    transition('closed => open', animate('200ms ease-in')),
    transition('open => closed', animate('100ms ease-out'))
  ])]
})
export class AppComponent implements OnInit {
  title = 'Yenkay Family Tree App';
  options = {
    timeOut: 3000
  };
  show:boolean = false;

  constructor(private route: Router, private location: Location) { }

  isIn = false;
  name: string;

  toggleState() { // click handler
      let bool = this.isIn;
      this.isIn = bool === false ? true : false;
  }

  toggleCollapse() {
    this.show = !this.show
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
