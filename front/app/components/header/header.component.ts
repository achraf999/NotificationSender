import { Router } from '@angular/router';
import { UserService } from './../../services/auth/user.service';
import { TokenStorageService } from './../../services/auth/token-storage.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  currentUser: null;

  constructor(private tokenStorageService: TokenStorageService, private user: UserService, private router: Router) { }

  logout(): void {
    this.tokenStorageService.remove();
    location.reload();
    this.router.navigateByUrl('/login')
  }

  ngOnInit(): void {
    this.user.authStatus.subscribe(res =>
      {
        this.currentUser = this.tokenStorageService.getInfos();
        console.log(this.currentUser)
      })
  }

}
