import { Router } from '@angular/router';
import { UserService } from './services/auth/user.service';
import { Component, OnInit } from '@angular/core';
import { TokenStorageService } from './services/auth/token-storage.service';
import { CommonModule } from '@angular/common';  
import { BrowserModule } from '@angular/platform-browser';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
 
  isLoggedIn = false;
  currentUser: null;

  constructor(private tokenStorageService: TokenStorageService, private user: UserService, private router: Router) { }

  ngOnInit(): void {
    this.user.authStatus.subscribe(res =>
      {
        this.currentUser = this.tokenStorageService.getInfos();
      })
  }

  logout(): void {
    this.tokenStorageService.remove();
    this.router.navigateByUrl('/login')
  }
}