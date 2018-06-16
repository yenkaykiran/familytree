import {Injectable} from "@angular/core";
import { ConnectionBackend, RequestOptions, Request, RequestOptionsArgs, Response, Http, Headers, XHRBackend} from "@angular/http";
import {Observable} from "rxjs/Rx";

import { TokenService } from '../services/token.service';

import { NotificationsService } from 'angular2-notifications';
import { Router } from '@angular/router';

@Injectable()
export class InterceptedHttp extends Http {

	constructor(backend: ConnectionBackend, defaultOptions: RequestOptions, private tokenService: TokenService, private notificationsService: NotificationsService, private router: Router) {
        super(backend, defaultOptions);
    }

    request(url: string | Request, options?: RequestOptionsArgs): Observable<Response> {
        return super.request(url, options);
    }

    get(url: string, options?: RequestOptionsArgs): Observable<Response> {
        url = this.updateUrl(url);
		return Observable.create(observer => {
            super.get(url, this.getRequestOptionArgs(options)).subscribe(
                res => observer.next(res), //simply passing success response
                err => { //error handling
						this.forward(err, observer);
                },
                () => observer.complete); //passing onComplete event);
        });
    }

    post(url: string, body: string, options?: RequestOptionsArgs): Observable<Response> {
        url = this.updateUrl(url);
		return Observable.create(observer => {
            super.post(url, body, this.getRequestOptionArgs(options)).subscribe(
                res => observer.next(res), //simply passing success response
                err => { //error handling
						this.forward(err, observer);
                },
                () => observer.complete); //passing onComplete event);
        });
    }

    put(url: string, body: string, options?: RequestOptionsArgs): Observable<Response> {
        url = this.updateUrl(url);
        return Observable.create(observer => {
            super.put(url, body, this.getRequestOptionArgs(options)).subscribe(
                res => observer.next(res), //simply passing success response
                err => { //error handling
						this.forward(err, observer);
                },
                () => observer.complete); //passing onComplete event);
        });
    }

    delete(url: string, options?: RequestOptionsArgs): Observable<Response> {
        url = this.updateUrl(url);
        return Observable.create(observer => {
            super.delete(url, this.getRequestOptionArgs(options)).subscribe(
                res => observer.next(res), //simply passing success response
                err => { //error handling
						this.forward(err, observer);
                },
                () => observer.complete); //passing onComplete event);
        });
    }

    private updateUrl(req: string) {
        return  req;
    }

    private getRequestOptionArgs(options?: RequestOptionsArgs) : RequestOptionsArgs {
        if (options == null) {
            options = new RequestOptions();
        }
        if (options.headers == null) {
            options.headers = new Headers();
        }
				if(null != this.tokenService.getToken()) {
	        options.headers.append("Authorization", "Bearer " + this.tokenService.getToken());
				}

        return options;
    }

	private forward(err, observer) {
		let message = err.json()['message'];
		if(message.indexOf('JWT expired') > -1 || err.status == 401) {
			this.notificationsService.error(message);
			this.router.navigate(["/login"]);
		}
		observer.error(err); //passing error to the method which invoked request
	}
}

export function httpFactory(xhrBackend: XHRBackend, requestOptions: RequestOptions, tokenService: TokenService, notificationsService: NotificationsService, router: Router): Http {
    return new InterceptedHttp(xhrBackend, requestOptions, tokenService, notificationsService, router);
}
