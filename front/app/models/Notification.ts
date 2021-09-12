import { ImpoMethod } from './enums/impo-method';
import { Frequence } from './enums/frequence';
import { Time } from '@angular/common';
import { NotificationType } from './enums/notification-type';
export interface Notification
{
    id: number;
    status: string,
    type: NotificationType;
    message: string;
    daysNum: number;
    destinataires: string[];
    frequence: Frequence;
    impoMethod: ImpoMethod;
    subject: string;
    date_envoi: Date;
    sendingTime:string


}