import * as dayjs from 'dayjs';
import { IDepartment } from '../department/department.model';

export interface IDocumentDelivery {
  id?: number;
  referenceNo?: string;
  sentDate?: dayjs.Dayjs;
  subject?: string;
  description?: string;
  deliveryStatus?: number;
  status?: number;
  delFlag?: string;
  sender?: IDepartment;
}

export class DocumentDelivery implements IDocumentDelivery {
  constructor(
    public id?: number,
    public referenceNo?: string,
    public sentDate?: dayjs.Dayjs,
    public subject?: string,
    public description?: string,
    public deliveryStatus?: number,
    public status?: number,
    public delFlag?: string,
    public sender?: IDepartment
  ) {}
}

export function getDeliveryIdentifier(delivery: IDocumentDelivery): number | undefined {
  return delivery.id;
}

export interface IDocumentReceiver {
  id?: number;
  receiverType?: number;
  status?: number;
  delFlag?: string;
  receiver?: IDepartment;
}

export class DocumentReceiver implements IDocumentReceiver {
  constructor(
    public id?: number,
    public receiverType?: number,
    public status?: number,
    public delFlag?: string,
    public receiver?: IDepartment
  ) {}
}

export interface IDocumentAttachment {
  id?: number;
  filePath?: string;
  fileName?: string;
  delFlag?: string;
}


export class DocumentAttachment implements IDocumentAttachment {
  constructor(public id?: number, public filePath?: string, public fileName?: string, public delFlag?: string) {}
}

export interface IDeliveryMessage {
  documentDelivery?: IDocumentDelivery;
  receiverList?: IDocumentReceiver[];
  attachmentList?: IDocumentAttachment[];
}

export class DeliveryMessage implements IDeliveryMessage {
  constructor(
    public documentDelivery?: IDocumentDelivery,
    public receiverList?: IDocumentReceiver[],
    public attachmentList?: IDocumentAttachment[]
  ) {}
}