import { Notification } from './../../models/Notification';
import { Observable } from 'rxjs';
import { AppConstants } from './../../common/app.constants';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class EmailService {

  selectedFile: File = new File([], '');

  constructor(private http: HttpClient) { }

  sendEmail(data: Notification): Observable<any>
  {
    return this.http.post(AppConstants.EMAIL_API, data);
  }

  uploadContactList(file: any):Observable<any>
  {
    return this.http.post(AppConstants.CONTACTS_API, file)
  }

  getEmailNotification(): Observable<any> {
    return this.http.get(AppConstants.EMAIL_API)
  }

  getEmailNotificationDetails(id: number): Observable<any> {
    return this.http.get(AppConstants.EMAIL_API + id)
  }

  deleteAllEmailNotifications(): Observable<any> {
    return this.http.delete(AppConstants.EMAIL_API)
  }

  deleteEmailNotification(id: number) {
    return this.http.delete(AppConstants.EMAIL_API + id) 
  }

  sendNow(data: Notification) {
    return this.http.post(AppConstants.EMAIL_API + "sendNow", data);

  }

  countEmail() {
    return this.http.get(AppConstants.EMAIL_API + "count")
  }
}
