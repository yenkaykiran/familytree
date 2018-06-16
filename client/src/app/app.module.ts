import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule } from '@angular/core';
import { HttpModule, Http, XHRBackend, RequestOptions } from '@angular/http';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { MemberComponent } from './member/member.component';
import { MemberService } from './member/member.service';

import { RouterModule, Routes } from '@angular/router';
import { MemberListComponent } from './member-list/member-list.component';
import { MemberEditComponent } from './member-edit/member-edit.component';
import { MemberLinkComponent } from './member-link/member-link.component';
import { MemberUnlinkComponent } from './member-unlink/member-unlink.component';
import { MemberListCytoComponent } from './member-list-cyto/member-list-cyto.component';
import { CytoscapeComponent } from './cytoscape/cytoscape.component';
import { ExportComponent } from './export/export.component';

import { SimpleNotificationsModule, NotificationsService } from 'angular2-notifications';

import { httpFactory } from "./interceptors/backend";
import { AuthGuard } from './guards/auth.guard';
import { AuthenticationService } from './services/authentication.service';
import { TokenService } from './services/token.service';

const appRoutes: Routes = [
  { path: '', component: MemberListComponent, canActivate: [AuthGuard] },
  { path: 'login', component: LoginComponent },
  { path: 'list/:name', component: MemberListComponent, canActivate: [AuthGuard] },
  { path: 'cyto-render/:name', component: MemberListCytoComponent, canActivate: [AuthGuard] },
  { path: 'export', component: ExportComponent, canActivate: [AuthGuard] },
  { path: '**', redirectTo: '' }
];

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    MemberComponent,
    MemberListComponent,
    MemberEditComponent,
    MemberLinkComponent,
    MemberUnlinkComponent,
    MemberListCytoComponent,
    CytoscapeComponent,
    ExportComponent
  ],
  imports: [
    FormsModule,
    BrowserModule,
    BrowserAnimationsModule,
    HttpModule,
    NgbModule.forRoot(),
    SimpleNotificationsModule.forRoot(),
    RouterModule.forRoot(
      appRoutes
      // , { enableTracing: true } // <-- debugging purposes only
    )
  ],
  providers: [
    HttpModule,
    MemberService,
    AuthGuard,
  	{
  		provide: Http,
          useFactory: httpFactory,
          deps: [XHRBackend, RequestOptions, TokenService, NotificationsService, Router]
      },
  	AuthenticationService,
  	TokenService,
  ],
  entryComponents: [
    MemberEditComponent,
    MemberLinkComponent
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
