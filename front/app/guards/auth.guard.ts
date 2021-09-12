import { UserService } from './../services/auth/user.service';
import { TokenStorageService } from './../services/auth/token-storage.service';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private tokenService: TokenStorageService, private user: UserService, private router: Router) {}
  canActivate(): boolean  {
      if(!this.tokenService.isAuthenticated()) {
        this.user.changeStatus(false);
        this.router.navigateByUrl('/login');
        return false;
      }
    return true;
  }
  
}
