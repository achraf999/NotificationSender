import { JournalService } from './../../services/journal.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-journal',
  templateUrl: './journal.component.html',
  styleUrls: ['./journal.component.css']
})
export class JournalComponent implements OnInit {

  constructor(private logService: JournalService) { }
  logs: any

  getLogs() {
    this.logService.getLogs().subscribe(res =>
      {
        this.logs = res
        console.log(this.logs)
      })
  }

  deleteLogs() {
    this.logService.deleteLogs().subscribe(res =>
      {
        console.log("delete LOGS")
      })
      location.reload();
  }

  ngOnInit(): void {
    this.getLogs()
  }

}
