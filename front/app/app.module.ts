import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { JwtHelperService, JwtInterceptor, JWT_OPTIONS  } from '@auth0/angular-jwt';


import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RegisterComponent } from './components/auth/register/register.component';
import { LoginComponent } from './components/auth/login/login.component';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { JournalComponent } from './components/journal/journal.component';
import { EditNotificationComponent } from './components/notifications/edit-notification/edit-notification.component';
import { HomeComponent } from './components/home/home.component';
import { SettingsComponent } from './components/settings/settings.component';
import { HeaderComponent } from './components/header/header.component';

import { ListEmailNotificationsComponent } from './components/notifications/email/list-email-notifications/list-email-notifications.component';
import { AddEmailNotificationsComponent } from './components/notifications/email/add-email-notifications/add-email-notifications.component';
import { EmailDetailsNotificationsComponent } from './components/notifications/email/email-details-notifications/email-details-notifications.component';
import { ListWtspNotificationsComponent } from './components/notifications/whatsapp/list-wtsp-notifications/list-wtsp-notifications.component';
import { AddWtspNotificationsComponent } from './components/notifications/whatsapp/add-wtsp-notifications/add-wtsp-notifications.component';
import { WtspNotificationsDetailsComponent } from './components/notifications/whatsapp/wtsp-notifications-details/wtsp-notifications-details.component';
import { ListSmsNotificationsComponent } from './components/notifications/sms/list-sms-notifications/list-sms-notifications.component';
import { AddSmsNotificationsComponent } from './components/notifications/sms/add-sms-notifications/add-sms-notifications.component';
import { SmsNotificationsDetailsComponent } from './components/notifications/sms/sms-notifications-details/sms-notifications-details.component';
import { StatsComponent } from './components/stats/stats.component';

@NgModule({
  declarations: [
    AppComponent,
    RegisterComponent,
    LoginComponent,
    JournalComponent,
    EditNotificationComponent,
    HomeComponent,
    SettingsComponent,
    HeaderComponent,
    ListEmailNotificationsComponent,
    AddEmailNotificationsComponent,
    EmailDetailsNotificationsComponent,
    ListWtspNotificationsComponent,
    AddWtspNotificationsComponent,
    WtspNotificationsDetailsComponent,
    ListSmsNotificationsComponent,
    AddSmsNotificationsComponent,
    SmsNotificationsDetailsComponent,
    StatsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    { provide: JWT_OPTIONS, useValue: JWT_OPTIONS },
    JwtHelperService],
  bootstrap: [AppComponent]
})
export class AppModule { }
