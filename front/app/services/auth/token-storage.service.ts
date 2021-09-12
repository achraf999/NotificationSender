import { Injectable } from '@angular/core';

import { JwtHelperService } from '@auth0/angular-jwt';




@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {

  constructor(private jwtHelper: JwtHelperService) { }


  // ...
  public isAuthenticated(): boolean {
    const token: any = this.getToken();
    // Check whether the token is expired and return
    // true or false
    return !this.jwtHelper.isTokenExpired(token);
  }



  public saveToken(data: any): void {
    localStorage.setItem('token', data.accessToken);
    localStorage.setItem('id', data.id);
  }

  public getToken() {
    return localStorage.getItem('token');
  }

  getId() {
      return localStorage.getItem('id')
  }

  remove() {
    
      localStorage.removeItem('token');
      localStorage.removeItem('id');
      
  }

  decode(payload: any) {
    return JSON.parse(atob(payload));
  }

  payload(token: any) {
    const payload = token.split('.')[1];
    return this.decode(payload);
  }

  isValid() {
      const token = this.getToken();
      const id = this.getId();

      if(token) {
        console.log(token)
          const payload = this.payload(token);
          if(payload) {
              return id == payload.id
              
          }
      }
      return false;
  }

  getInfos() {
      const token = this.getToken();

      if(token) {
          const payload = this.payload(token);
          return payload ? payload : null;
      }
  }

  loggedIn() {
      return this.isValid()
  }

  public getUser(): any {
    return true;
  }








}