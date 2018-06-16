import { Injectable } from '@angular/core';
import { Http, Response, Headers } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';

import { TokenService } from './token.service';

@Injectable()
export class AuthenticationService {

  constructor(private http: Http, private tokenService: TokenService) { }

    login(username: string, password: string) {
		let heads = new Headers();
		heads.append("Authorization", "Basic " + btoa(username + ":" + password));
        return this.http
				   .post('/api/login', null, {headers: heads});
    }

    logout() {
        // remove user from local storage to log user out
        this.tokenService.removeToken();
    }
}
