import { ActivatedRoute } from '@angular/router';
import { WhatsappService } from './../../../../services/notifications/whatsapp.service';
import { Component, OnInit } from '@angular/core';
import { Notification } from 'src/app/models/Notification';

@Component({
  selector: 'app-wtsp-notifications-details',
  templateUrl: './wtsp-notifications-details.component.html',
  styleUrls: ['./wtsp-notifications-details.component.css']
})
export class WtspNotificationsDetailsComponent implements OnInit {

  constructor( private whatsappSender: WhatsappService, private route: ActivatedRoute ) { }
  id: number;
  whatsapp: Notification;


  
  getWhatsappNotificationDetails() {
    this.id = this.route.snapshot.params['id'];
    this.whatsappSender.getEmailNotificationDetails(this.id).subscribe(res =>
     {
       this.whatsapp = res
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
    this.whatsappSender.sendNow(this.whatsapp).subscribe(res =>
      {
        alert('Whatsapp Sent successfully');
        console.log("Whatsapp sent");
        location.reload();
   
       },
       err => {
         alert('An error has occured while sending the Whatsapp notification');
       });
  }

  ngOnInit(): void {
    this.getWhatsappNotificationDetails()
  }

}
