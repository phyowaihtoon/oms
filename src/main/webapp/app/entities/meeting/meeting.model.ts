import * as dayjs from 'dayjs';
import { IDepartment } from '../department/department.model';

export interface IMeetingDelivery {
  id?: number;
  referenceNo?: string;
  sentDate?: dayjs.Dayjs;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs;
  place?: string;
  subject?: string;
  description?: string;
  deliveryStatus?: number;
  meetingStatus?: number;
  status?: number;
  delFlag?: string;
  sender?: IDepartment;
}

export class MeetingDelivery implements IMeetingDelivery {
  constructor(
    public id?: number,
    public referenceNo?: string,
    public sentDate?: dayjs.Dayjs,
    public startDate?: dayjs.Dayjs,
    public endDate?: dayjs.Dayjs,
    public place?: string,
    public subject?: string,
    public description?: string,
    public deliveryStatus?: number,
    public meetingStatus?: number,
    public status?: number,
    public delFlag?: string,
    public sender?: IDepartment
  ) {}
}

export function getMeetingIdentifier(delivery: IMeetingDelivery): number | undefined {
  return delivery.id;
}

export interface IMeetingReceiver {
  id?: number;
  receiverType?: number;
  status?: number;
  delFlag?: string;
  receiver?: IDepartment;
}

export class MeetingReceiver implements IMeetingReceiver {
  constructor(
    public id?: number,
    public receiverType?: number,
    public status?: number,
    public delFlag?: string,
    public receiver?: IDepartment
  ) {}
}

export interface IMeetingAttachment {
  id?: number;
  filePath?: string;
  fileName?: string;
  delFlag?: string;
}

export class MeetingAttachment implements IMeetingAttachment {
  constructor(public id?: number, public filePath?: string, public fileName?: string, public delFlag?: string) {}
}

export interface IMeetingMessage {
  meetingDelivery?: IMeetingDelivery;
  receiverList?: IMeetingReceiver[];
  attachmentList?: IMeetingAttachment[];
}

export class MeetingMessage implements IMeetingMessage {
  constructor(
    public meetingDelivery?: IMeetingDelivery,
    public receiverList?: IMeetingReceiver[],
    public attachmentList?: IMeetingAttachment[]
  ) {}
}
