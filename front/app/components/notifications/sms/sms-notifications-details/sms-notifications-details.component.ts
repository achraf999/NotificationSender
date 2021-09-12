import { Router, ActivatedRoute } from '@angular/router';
import { SmsService } from './../../../../services/notifications/sms.service';
import { Component, OnInit } from '@angular/core';
import { Notification } from 'src/app/models/Notification';

@Component({
  selector: 'app-sms-notifications-details',
  templateUrl: './sms-notifications-details.component.html',
  styleUrls: ['./sms-notifications-details.component.css']
})
export class SmsNotificationsDetailsComponent implements OnInit {
  id: number
  sms: Notification

  constructor( private smsSender: SmsService, private route: ActivatedRoute ) { }


  getSmsNotificationDetails() {
    this.id = this.route.snapshot.params['id'];
    this.smsSender.getSmsNotificationDetails(this.id).subscribe(res =>
     {
       this.sms = res;
       console.log(this.sms)
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
    this.smsSender.sendNow(this.sms).subscribe(res =>
      {
        alert('SMS Sent successfully');
        console.log("sms sent");
        location.reload();
   
       },
       err => {
         alert('An error has occured while sending SMS');
       });
  }

  ngOnInit(): void {
    this.getSmsNotificationDetails()
  }

}
