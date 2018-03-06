import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpModule } from '@angular/http';
import { FormsModule } from '@angular/forms';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { AppComponent } from './app.component';
import { MemberComponent } from './member/member.component';
import { MemberService } from './member/member.service';

import { RouterModule, Routes } from '@angular/router';
import { MemberListComponent } from './member-list/member-list.component';
import { MemberEditComponent } from './member-edit/member-edit.component';
import { MemberLinkComponent } from './member-link/member-link.component';

@NgModule({
  declarations: [
    AppComponent,
    MemberComponent,
    MemberListComponent,
    MemberEditComponent,
    MemberLinkComponent
  ],
  imports: [
    FormsModule,
    BrowserModule,
    HttpModule,
    NgbModule.forRoot(),
    RouterModule.forRoot(
      [],
      { enableTracing: true } // <-- debugging purposes only
    )
  ],
  providers: [
    HttpModule,
    MemberService
  ],
  entryComponents: [
    MemberEditComponent,
    MemberLinkComponent
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
