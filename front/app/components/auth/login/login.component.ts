import { UserService } from './../../../services/auth/user.service';
import { TokenStorageService } from '../../../services/auth/token-storage.service';
import { AuthService } from '../../../services/auth/auth.service';
import { Component, OnInit } from '@angular/core';

import { ActivatedRoute, Router } from '@angular/router';



@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  form: any = {};
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';
  currentUser: any;


  constructor(private authService: AuthService, private tokenStorage: TokenStorageService, private route: Router, private userService: UserService) {}

  ngOnInit(): void {
	// // const token: string = this.route.snapshot.queryParamMap.get('token');
	// // const error: string = this.route.snapshot.queryParamMap.get('error');
  // 	if (this.tokenStorage.getToken()) {
  //     this.isLoggedIn = true;
  //     this.currentUser = this.tokenStorage.getUser();
  //   }
  // 	else if(token){
  // 		this.tokenStorage.saveToken(token);
  // 		this.userService.getCurrentUser().subscribe(
  // 		      data => {
  // 		        this.login(data);
  // 		      },
  // 		      err => {
  // 		        this.errorMessage = err.error.message;
  // 		        this.isLoginFailed = true;
  // 		      }
  // 		  );
  // 	}
  // 	else if(error){
  // 		this.errorMessage = error;
	//     this.isLoginFailed = true;
  // 	}
  }

  onSubmit(): void {
    this.authService.login(this.form).subscribe(
      data => {
        this.handleResponse(data);
      },
      err => {
        this.errorMessage = err.error.message;
        this.isLoginFailed = true;
      }
    );
  }

  // login(user: any): void {
	// this.tokenStorage.saveUser(user);
	// this.isLoginFailed = false;
	// this.isLoggedIn = true;
	// this.currentUser = this.tokenStorage.getUser();
  //   window.location.reload();
  // }

  handleResponse(res: any) {
    this.tokenStorage.saveToken(res)
    this.userService.changeStatus(true)
    this.route.navigateByUrl('home')
    this.isLoggedIn = true
  }

}