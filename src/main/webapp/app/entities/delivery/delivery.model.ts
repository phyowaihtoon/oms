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
  createdDate?: dayjs.Dayjs;
  receiverType?: number; // This filed data is not needed to pass to server side
  receiverList?: IReceiverInfo[]; // This filed data is not needed to pass to server side
  rowExpanded?: boolean; // This filed data is not needed to pass to server side
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
    public sender?: IDepartment,
    public createdDate?: dayjs.Dayjs,
    public receiverType?: number,
    public receiverList?: IReceiverInfo[],
    public rowExpanded?: boolean
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
  fileData?: File; // This filed data is not needed to pass to server side, only used in client side
}

export interface IReceiverInfo {
  departmentId?: number;
  receiverType?: number;
  departmentName?: string;
}

export class DocumentAttachment implements IDocumentAttachment {
  constructor(public id?: number, public filePath?: string, public fileName?: string, public delFlag?: string, public fileData?: File) {}
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
