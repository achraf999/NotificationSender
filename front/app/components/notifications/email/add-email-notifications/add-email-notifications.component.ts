import { Router } from '@angular/router';
import { SmsService } from './../../../../services/notifications/sms.service';
import { WhatsappService } from './../../../../services/notifications/whatsapp.service';
import { EmailService } from 'src/app/services/notifications/email.service';
import { ImpoMethod } from './../../../../models/enums/impo-method';
import { Frequence } from 'src/app/models/enums/frequence';
import { NotificationType } from 'src/app/models/enums/notification-type';
import { Component, OnInit } from '@angular/core';
import { Notification } from 'src/app/models/Notification';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-add-email-notifications',
  templateUrl: './add-email-notifications.component.html',
  styleUrls: ['./add-email-notifications.component.css']
})
export class AddEmailNotificationsComponent implements OnInit {

  selectedValue: any;
  csvRecords: any[] = [];
  header: boolean = false;
  options: any;
  selectedFile: File = new File([], '') ;
  csv: boolean = false;
  oneShot: boolean = true;
  noti_type: any


  notification: Notification = {
    id: 0,
    status: '',
    type: NotificationType.EMAIL,
    message: '',
    daysNum: 0,
    destinataires: [],
    frequence: Frequence.NON_PERIODIQUE,
    impoMethod: ImpoMethod.MANUELLLE,
    subject: '',
    date_envoi: new Date(),
    sendingTime: new Date().getHours() + ':' + new Date().getMinutes()

    
  }

  constructor(private emailSender: EmailService, private router: Router
              ) { }

  public onFileChanged(event: any) {
    this.selectedFile = event.target.files[0];
  }

  onUpload()
  {
    if(!this.selectedFile) 
    this.csv = true;
    //FormData API provides methods and properties to allow us easily prepare form data to be sent with POST HTTP requests.
    const uploadImageData = new FormData();
    uploadImageData.append('file', this.selectedFile, this.selectedFile.name);
    console.log(this.selectedFile);
    this.emailSender.uploadContactList(uploadImageData).subscribe(res =>
      {
        console.log("res" + res.status)
        Swal.fire({
          position: 'top-end',
          icon: "success",
          title: 'Le fichier a été importé avec succès',
          showConfirmButton: false,
          timer: 2500
        })
      },
         
     
      err =>
      {
        Swal.fire({
          position: 'top-end',
          icon: "error",
          title: 'Erreur survenue',
          showConfirmButton: false,
          timer: 2500
        })
      
      });
  
  }

  scheduleEmailNotification() {
    const non_perio = Frequence.NON_PERIODIQUE
    const perio = Frequence.PERIODIQUE
    

    if (this.oneShot) {
      this.notification.frequence = non_perio
      console.log("one shot process")
    }

    else if (!this.oneShot) {
      this.notification.frequence= perio
      console.log("periodic process")
    }

    

    this.emailSender.sendEmail(this.notification).subscribe( res =>
      {
        console.log(this.notification.frequence.toString());
        Swal.fire({
          position: 'top-end',
          icon: "success",
          title: 'La notification a été Programmé avec succès',
          showConfirmButton: false,
          timer: 2500
        })
      },

     err => {
      Swal.fire({
        position: 'top-end',
        icon: "error",
        title: 'Une erreur est survenue',
        showConfirmButton: false,
        timer: 2500
      })
     });
     
     this.router.navigateByUrl("/email/list-notifications")

  }


  ngOnInit(): void {
  }

}
