import { SmsService } from './../../../../services/notifications/sms.service';
import { WhatsappService } from './../../../../services/notifications/whatsapp.service';
import { EmailService } from 'src/app/services/notifications/email.service';
import { ImpoMethod } from './../../../../models/enums/impo-method';
import { NotificationType } from 'src/app/models/enums/notification-type';

import { Frequence } from 'src/app/models/enums/frequence';

import { Component, OnInit } from '@angular/core';
import { Notification } from 'src/app/models/Notification';


@Component({
  selector: 'app-list-email-notifications',
  templateUrl: './list-email-notifications.component.html',
  styleUrls: ['./list-email-notifications.component.css']
})
export class ListEmailNotificationsComponent implements OnInit {
  notifications: any[]
  notification: Notification = {
    id: 0,
    type: NotificationType.EMAIL,
    status: '',
    message: '',
    daysNum: 0,
    destinataires: [],
    frequence: Frequence.NON_PERIODIQUE,
    impoMethod: ImpoMethod.MANUELLLE,
    subject: '',
    date_envoi: new Date(),
    sendingTime: new Date().getHours() + ':' + new Date().getMinutes()

    
  }


  constructor( private emailSender: EmailService ) { }

  getEmailNotification() {
    this.emailSender.getEmailNotification().subscribe(res =>
      {
        this.notifications = res;
      })
  }

  getEmailNotificationDetails(id: number) {
   this.emailSender.getEmailNotificationDetails(id).subscribe(res =>
    {

    })
  }

  deleteAllEmailNotifications() {
    this.emailSender.deleteAllEmailNotifications().subscribe(res =>
      {
        console.log(res)
      })
  }

  deleteEmailNotification(id: number) {
    this.emailSender.deleteEmailNotification(id).subscribe(res =>
      {
        console.log(res)
      })
      location.reload();
  }

  sendNotificationNow() {
    this.emailSender.sendNow(this.notification).subscribe(res =>
      {
        alert('Email Sent successfully');
        console.log("email sent");
        location.reload();
   
       },
       err => {
         alert('An error has occured while sending email');
       });
  }

  editNotification(id: number) {

  }

  ngOnInit(): void {
    this.getEmailNotification()
  }

}
