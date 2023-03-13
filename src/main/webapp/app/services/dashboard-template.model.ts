export interface IDashboardTemplate {
  id?: number;
  cardId?: string;
  cardName?: string;
  cardType?: string;
  serviceUrl?: string;
  nameInMyanmar?: string;
}

export class DashboardTemplate implements IDashboardTemplate {
  constructor(
    public id?: number,
    public cardId?: string,
    public cardName?: string,
    public cardType?: string,
    public serviceUrl?: string,
    public nameInMyanmar?: string
  ) {}
}
