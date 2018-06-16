import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs/Observable';

import { TokenService } from '../services/token.service';


@Injectable()
export class AuthGuard implements CanActivate {
  
  constructor(private router: Router, private tokenService: TokenService) { }
  
  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    if (this.tokenService.isTokenValid()) {
		// logged in so return true
		return true;
	}

	// not logged in so redirect to login page with the return url
	this.router.navigate(['/login'], { queryParams: { returnUrl: state.url }});
	return false;
  }
}
