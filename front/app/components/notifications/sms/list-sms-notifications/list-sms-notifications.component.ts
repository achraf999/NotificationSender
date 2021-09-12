import { SmsService } from './../../../../services/notifications/sms.service';
import { ImpoMethod } from './../../../../models/enums/impo-method';
import { Frequence } from './../../../../models/enums/frequence';
import { NotificationType } from './../../../../models/enums/notification-type';
import { Component, OnInit } from '@angular/core';
import { Notification } from 'src/app/models/Notification';

@Component({
  selector: 'app-list-sms-notifications',
  templateUrl: './list-sms-notifications.component.html',
  styleUrls: ['./list-sms-notifications.component.css']
})
export class ListSmsNotificationsComponent implements OnInit {

  constructor( private smsSender: SmsService) { }


notifications: any[] = []
  notification: Notification = {
    id: 0,
    status: '',
    type: NotificationType.SMS,
    message: '',
    daysNum: 0,
    destinataires: [],
    frequence: Frequence.NON_PERIODIQUE,
    impoMethod: ImpoMethod.MANUELLLE,
    subject: '',
    date_envoi: new Date(),
    sendingTime: new Date().getHours() + ':' + new Date().getMinutes()

    
  }

  getSmsNotification() {
    this.smsSender.getSmsNotifications().subscribe(res =>
      {
        this.notifications = res;
      })
  }

  getSmsNotificationDetails(id: number) {
   this.smsSender.getSmsNotificationDetails(id).subscribe(res =>
    {
      console.log(res)
    })
  }

  deleteAllSmsNotifications() {
    this.smsSender.deleteAllSmsNotifications().subscribe(res =>
      {
        console.log(res)
      })
  }

  deleteSmsNotification(id: number) {
    this.smsSender.deleteSmsNotification(id).subscribe(res =>
      {
        console.log(res)
      })
      location.reload();
  }

  sendNotificationNow() {
    this.smsSender.sendNow(this.notification).subscribe(res =>
      {
        alert('SMS Sent successfully');
        console.log("sms sent");
        location.reload();
   
       },
       err => {
         alert('An error has occured while sending SMS');
       });
  }

  editNotification(id: number) {

  }

  ngOnInit(): void {
    this.getSmsNotification()
  }

}
