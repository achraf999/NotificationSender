import { TokenStorageService } from './token-storage.service';
import { AppConstants } from './../../common/app.constants';
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';

const httpOptions = {
		  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
		};


@Injectable({
  providedIn: 'root'
})
export class UserService {
  private loggedIn = new BehaviorSubject<boolean>(this.tokenService.loggedIn());

  authStatus = this.loggedIn.asObservable();

  constructor(private tokenService: TokenStorageService) { }

  changeStatus(value: boolean) {
    this.loggedIn.next(value)
  }


  // getPublicContent(): Observable<any> {
  //   return this.http.get(AppConstants.API_URL + 'all', { responseType: 'text' });
  // }

  // getUserBoard(): Observable<any> {
  //   return this.http.get(AppConstants.API_URL + 'user', { responseType: 'text' });
  // }

  // getModeratorBoard(): Observable<any> {
  //   return this.http.get(AppConstants.API_URL + 'mod', { responseType: 'text' });
  // }

  // getAdminBoard(): Observable<any> {
  //   return this.http.get(AppConstants.API_URL + 'admin', { responseType: 'text' });
  // }

  // getCurrentUser(): Observable<any> {
  //   return this.http.get(AppConstants.API_URL + 'user/me', httpOptions);
  // }
}