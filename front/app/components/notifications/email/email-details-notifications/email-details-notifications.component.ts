import { ActivatedRoute } from '@angular/router';
import { EmailService } from 'src/app/services/notifications/email.service';
import { Component, OnInit } from '@angular/core';
import { Notification } from 'src/app/models/Notification';

@Component({
  selector: 'app-email-details-notifications',
  templateUrl: './email-details-notifications.component.html',
  styleUrls: ['./email-details-notifications.component.css']
})
export class EmailDetailsNotificationsComponent implements OnInit {

  constructor( private emailNotiService: EmailService, private route: ActivatedRoute ) { }

  id: number = 0;
  emailNoti: Notification 
  
    getNotificationDetails()
    {
      this.id = this.route.snapshot.params['id'];
      
      this.emailNotiService.getEmailNotificationDetails(this.id).subscribe( res =>
        {
          this.emailNoti = res;
          console.log(this.emailNoti);
        })
    }
  
    deleteEmailNotification(id: number) {
      this.emailNotiService.deleteEmailNotification(id).subscribe(res =>
        {
          console.log(res)
        })
        location.reload();
    }
  
    sendNotificationNow() {
      this.emailNotiService.sendNow(this.emailNoti).subscribe(res =>
        {
          alert('Email Sent successfully');
          console.log("email sent");
          location.reload();
     
         },
         err => {
           alert('An error has occured while sending email');
         });
    }
  
  
    ngOnInit(): void {
      this.getNotificationDetails()
    }
  
  }
  
