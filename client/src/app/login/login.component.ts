import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

import { NotificationsService } from 'angular2-notifications';
import { AuthenticationService } from '../services/authentication.service';

import { TokenService } from '../services/token.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  model: any = {};
  loading = false;
  returnUrl: string;
  
  constructor(
		private route: ActivatedRoute,
        private router: Router,
        private authenticationService: AuthenticationService,
        private notificationsService: NotificationsService,
		private tokenService: TokenService) { }

  ngOnInit() {
	this.authenticationService.logout();

    // get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }
  
  login() {
        this.loading = true;
        this.authenticationService.login(this.model.username, this.model.password)
            .subscribe(response => {
					if (response && response.headers.has(this.tokenService.TOKEN_NAME)) {
						// store user details and jwt token in local storage to keep user logged in between page refreshes
						this.tokenService.saveToken(response);
						this.notificationsService.success("Login Success");
						this.router.navigate([this.returnUrl]);
					}
                },
                error => {
					this.loading = false;
					try {
						let err=error.json();
						this.notificationsService.error(err.message);
					} catch(e) {
						this.notificationsService.error(error.statusText);
					}
                });
    }
}
