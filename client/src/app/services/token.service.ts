import { Injectable } from '@angular/core';

@Injectable()
export class TokenService {

  constructor() { }

  public TOKEN_NAME = "jwttoken";

  public getToken() {
	  return localStorage.getItem(this.TOKEN_NAME);
  }

  public saveToken(response) {
	  localStorage.setItem(this.TOKEN_NAME, response.headers.get(this.TOKEN_NAME));
  }

  public isTokenValid() {
	  return this.getToken() != null;
  }

  public removeToken() {
	  localStorage.removeItem(this.TOKEN_NAME);
  }
}
