import { Notification } from './../../models/Notification';
import { AppConstants } from './../../common/app.constants';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SmsService {

  constructor(private http: HttpClient) { }

  sendSms(data: Notification): Observable<any>
  {
    return this.http.post(AppConstants.SMS_API, data);
  }

  getSmsNotifications(): Observable<any> {
    return this.http.get(AppConstants.SMS_API);
  }

  uploadContactList(file: any):Observable<any>
  {
    return this.http.post(AppConstants.CONTACTS_API, file)
  }


  getSmsNotificationDetails(id: number): Observable<any> {
    return this.http.get(AppConstants.SMS_API + id)
  }

  deleteAllSmsNotifications(): Observable<any> {
    return this.http.delete(AppConstants.SMS_API)
  }

  deleteSmsNotification(id: number) {
    return this.http.delete(AppConstants.SMS_API + id) 
  }

  sendNow(data: Notification) {
    return this.http.post(AppConstants.SMS_API + "sendNow", data);

  }
}
