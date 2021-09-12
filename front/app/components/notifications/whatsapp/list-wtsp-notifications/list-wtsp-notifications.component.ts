import { WhatsappService } from './../../../../services/notifications/whatsapp.service';
import { ImpoMethod } from './../../../../models/enums/impo-method';
import { Frequence } from './../../../../models/enums/frequence';
import { NotificationType } from './../../../../models/enums/notification-type';
import { Component, OnInit } from '@angular/core';
import { Notification } from 'src/app/models/Notification';

@Component({
  selector: 'app-list-wtsp-notifications',
  templateUrl: './list-wtsp-notifications.component.html',
  styleUrls: ['./list-wtsp-notifications.component.css']
})
export class ListWtspNotificationsComponent implements OnInit {

  constructor( private whatsappSender: WhatsappService) { }
  notifications: any[] = []
  notification: Notification = {
    id: 0,
    status,
    type: NotificationType.WHATSAPP,
    message: '',
    daysNum: 0,
    destinataires: [],
    frequence: Frequence.NON_PERIODIQUE,
    impoMethod: ImpoMethod.MANUELLLE,
    subject: '',
    date_envoi: new Date(),
    sendingTime: new Date().getHours() + ':' + new Date().getMinutes() 
  }

  getWhatsappNotification() {
    this.whatsappSender.getEmailNotification().subscribe(res =>
      {
        this.notifications = res;
      })
  }


  deleteAllWhatsappNotifications() {
    this.whatsappSender.deleteAllEmailNotifications().subscribe(res =>
      {
        console.log(res)
      })
  }

  deleteWhatsappNotification(id: number) {
    this.whatsappSender.deleteEmailNotification(id).subscribe(res =>
      {
        console.log(res)
      })
      location.reload();
  }

  sendNotificationNow() {
    this.whatsappSender.sendNow(this.notification).subscribe(res =>
      {
        alert('Whatsapp Sent successfully');
        console.log("Whatsapp sent");
        location.reload();
   
       },
       err => {
         alert('An error has occured while sending the Whatsapp notification');
       });
  }

  editNotification(id: number) {

  }

  ngOnInit(): void {
    this.getWhatsappNotification()
  }




}

