import { StatsComponent } from './components/stats/stats.component';
import { SmsNotificationsDetailsComponent } from './components/notifications/sms/sms-notifications-details/sms-notifications-details.component';
import { AddSmsNotificationsComponent } from './components/notifications/sms/add-sms-notifications/add-sms-notifications.component';
import { ListSmsNotificationsComponent } from './components/notifications/sms/list-sms-notifications/list-sms-notifications.component';
import { WtspNotificationsDetailsComponent } from './components/notifications/whatsapp/wtsp-notifications-details/wtsp-notifications-details.component';
import { AddWtspNotificationsComponent } from './components/notifications/whatsapp/add-wtsp-notifications/add-wtsp-notifications.component';
import { ListWtspNotificationsComponent } from './components/notifications/whatsapp/list-wtsp-notifications/list-wtsp-notifications.component';
import { EmailDetailsNotificationsComponent } from './components/notifications/email/email-details-notifications/email-details-notifications.component';
import { AddEmailNotificationsComponent } from './components/notifications/email/add-email-notifications/add-email-notifications.component';
import { HomeComponent } from './components/home/home.component';



import { RegisterComponent } from './components/auth/register/register.component';
import { LoginComponent } from './components/auth/login/login.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ListEmailNotificationsComponent } from './components/notifications/email/list-email-notifications/list-email-notifications.component';
import { AfterAuthGuard } from './guards/after-auth.guard';
import { AuthGuard } from './guards/auth.guard';
import { JournalComponent } from './components/journal/journal.component';
import { SettingsComponent } from './components/settings/settings.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent, canActivate: [AfterAuthGuard] },
  { path: 'register', component: RegisterComponent, canActivate: [AfterAuthGuard] },
  { path: 'home', component: HomeComponent, canActivate: [AuthGuard] },
  { path: 'settings', component: SettingsComponent, canActivate: [AuthGuard] },
  { path: 'email', canActivate: [AuthGuard], children:
    [
      { path: 'list-notifications', component: ListEmailNotificationsComponent},
      { path: 'add-notifications', component: AddEmailNotificationsComponent},
      { path: 'notification-details/:id', component: EmailDetailsNotificationsComponent}

    ] 
  },
  { path: 'whatsapp', canActivate: [AuthGuard], children:
    [
      { path: 'list-notifications', component: ListWtspNotificationsComponent},
      { path: 'add-notifications', component: AddWtspNotificationsComponent},
      { path: 'notification-details/:id', component: WtspNotificationsDetailsComponent}

    ] 
  },

  { path: 'sms', canActivate: [AuthGuard], children:
    [
      { path: 'list-notifications', component: ListSmsNotificationsComponent},
      { path: 'add-notifications', component: AddSmsNotificationsComponent},
      { path: 'notification-details/:id', component: SmsNotificationsDetailsComponent}
    ] 
  },
  { path: 'stats', component: StatsComponent, canActivate: [AuthGuard] },
  { path: 'journal', component: JournalComponent, canActivate: [AuthGuard] },
  { path: '', redirectTo: 'home', pathMatch: 'full' }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
