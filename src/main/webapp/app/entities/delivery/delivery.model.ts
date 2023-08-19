import * as dayjs from 'dayjs';

export interface IDelivery {
  id?: number;
  sender_id?: number;
  reference_no?: string;
  sent_date?: dayjs.Dayjs;
  description?: string;
  delivery_status?: number;
  status?: number;
  del_flag?: string;
  created_by?: string;
  created_date?: dayjs.Dayjs;
  last_modified_by?: dayjs.Dayjs;
  last_modified_date?: dayjs.Dayjs;
}


export class Delivery implements IDelivery {
  constructor(
    public id?: number, 
    public sender_id?: number,
    public reference_no?: string,
    public sent_date?: dayjs.Dayjs,
    public description?: string,
    public delivery_status?: number,
    public status?: number,
    public del_flag?: string,
    public created_by?: string,
    public created_date?: dayjs.Dayjs,
    public last_modified_by?: dayjs.Dayjs,
    public last_modified_date?: dayjs.Dayjs    
    ) {}
}

export function getDeliveryIdentifier(department: IDelivery): number | undefined {
  return department.id;
}
