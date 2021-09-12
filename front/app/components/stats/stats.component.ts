import { EmailService } from 'src/app/services/notifications/email.service';
import { Component, Input, OnInit } from '@angular/core';
import { Chart, registerables} from 'chart.js';

@Component({
  selector: 'app-stats',
  templateUrl: './stats.component.html',
  styleUrls: ['./stats.component.css']
})
export class StatsComponent implements OnInit {

  //@Input('my-id') myId = ''

  emailNum : any
  constructor( private emailSender: EmailService  ) {
    Chart.register(...registerables);

   }

  ngOnInit() {
    
  }



  ngAfterViewInit() {
    var ctx = 'myChart';
    this.emailSender.countEmail().subscribe(res =>
      {
        this.emailNum = res
      
    console.log(this.emailNum)
    var chart = new Chart(ctx, {
    type: 'pie',
    data: {
        labels: ['Email', 'WHATSAPP', 'SMS'],
        datasets: [{
            label: '# of Votes',
            data: [this.emailNum[0], this.emailNum[1], this.emailNum[2]],
            backgroundColor: [
                'rgba(255, 99, 132, 0.2)',
                'rgba(54, 162, 235, 0.2)',
                'rgba(255, 206, 86, 0.2)',
                'rgba(75, 192, 192, 0.2)',
                'rgba(153, 102, 255, 0.2)',
                'rgba(255, 159, 64, 0.2)'
            ],
            borderColor: [
                'rgba(255, 99, 132, 1)',
                'rgba(54, 162, 235, 1)',
                'rgba(255, 206, 86, 1)',
                'rgba(75, 192, 192, 1)',
                'rgba(153, 102, 255, 1)',
                'rgba(255, 159, 64, 1)'
            ],
            borderWidth: 1
        }]
    },
    options: {
        scales: {
            y: {
                beginAtZero: true
            }
        }
    }
});
})

  }



}
