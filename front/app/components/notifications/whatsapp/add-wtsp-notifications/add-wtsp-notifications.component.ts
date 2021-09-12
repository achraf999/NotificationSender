import { Router } from '@angular/router';
import { ImpoMethod } from './../../../../models/enums/impo-method';
import { NotificationType } from './../../../../models/enums/notification-type';
import { Frequence } from './../../../../models/enums/frequence';
import { WhatsappService } from './../../../../services/notifications/whatsapp.service';
import { Component, OnInit } from '@angular/core';
import { Notification } from 'src/app/models/Notification';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-add-wtsp-notifications',
  templateUrl: './add-wtsp-notifications.component.html',
  styleUrls: ['./add-wtsp-notifications.component.css']
})
export class AddWtspNotificationsComponent implements OnInit {

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

  constructor(private whatsappSender: WhatsappService, private router: Router
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
    this.whatsappSender.uploadContactList(uploadImageData).subscribe(res =>
      {
        console.log("res" + res.status)
        Swal.fire({
          position: 'top-end',
          icon: "success",
          title: 'Le fichier a été importé avec succès',
          showConfirmButton: false,
          timer: 2500
        })   
      })
  }

  scheduleWhatsappNotification() {
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

    

    this.whatsappSender.sendWhatsapp(this.notification).subscribe( res =>
      {
        console.log(this.notification.frequence.toString());
        console.log("res" + res.status)
        Swal.fire({
          position: 'top-end',
          icon: "success",
          title: 'La notification a été programmée avec succès',
          showConfirmButton: false,
          timer: 2500
        })
        this.router.navigateByUrl("/sms/list-notifications")      
      },
     err => {
      Swal.fire({
        position: 'top-end',
        icon: "error",
        title: 'Erreur survenue',
        showConfirmButton: false,
        timer: 2500
     })

  });
  }


  ngOnInit(): void {
  }

}
