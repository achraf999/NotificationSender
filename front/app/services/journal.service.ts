import { AppConstants } from './../common/app.constants';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class JournalService {

  constructor(private http: HttpClient) { }

  getLogs(): Observable<any> {
    return this.http.get(AppConstants.LOGS_API)
  }

  deleteLogs() {
    return this.http.delete(AppConstants.LOGS_API)
  }
}
