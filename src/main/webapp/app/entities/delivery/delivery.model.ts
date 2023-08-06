export interface IDelivery {
  id?: number;
  delFlag?: string;
}

export class Delivery implements IDelivery {
  constructor(public id?: number, public delFlag?: string) {}
}

export function getDeliveryIdentifier(department: IDelivery): number | undefined {
  return department.id;
}
