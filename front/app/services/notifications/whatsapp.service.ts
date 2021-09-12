import { AppConstants } from './../../common/app.constants';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Notification } from 'src/app/models/Notification';

@Injectable({
  providedIn: 'root'
})
export class WhatsappService {

  constructor( private http: HttpClient) { }

  sendWhatsapp(data: Notification): Observable<any>
  {
    return this.http.post(AppConstants.WHATSAPP_API, data);
  }

  getWhatsappNotifications(): Observable<any> {
    return this.http.get(AppConstants.WHATSAPP_API);
  }

  uploadContactList(file: any):Observable<any>
  {
    return this.http.post(AppConstants.CONTACTS_API, file)
  }

  getEmailNotification(): Observable<any> {
    return this.http.get(AppConstants.WHATSAPP_API)
  }

  getEmailNotificationDetails(id: number): Observable<any> {
    return this.http.get(AppConstants.WHATSAPP_API + id)
  }

  deleteAllEmailNotifications(): Observable<any> {
    return this.http.delete(AppConstants.WHATSAPP_API)
  }

  deleteEmailNotification(id: number) {
    return this.http.delete(AppConstants.WHATSAPP_API + id) 
  }

  sendNow(data: Notification) {
    return this.http.post(AppConstants.WHATSAPP_API + "sendNow", data);

  }

}
